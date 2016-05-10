package com.shundian.sshlogin;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * SSH工具类
 */
public class Main {

	private static final String serverUrl = "s";
	private static final String serverUserName = "u";
	private static final String serverPassword = "p";
	private static final String serverPort = "pt";
	private static final String executeFile = "f";

	public static String exec(String host, String user, String psw, String port, String command) {
		String result = "";
		Session session = null;
		ChannelExec openChannel = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(user, host, Integer.parseInt(port));
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setPassword(psw);
			session.connect();
			openChannel = (ChannelExec) session.openChannel("exec");
			openChannel.setCommand(command);
			int exitStatus = openChannel.getExitStatus();
			System.out.println(exitStatus);
			openChannel.connect();
			InputStream in = openChannel.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String buf = null;
			while ((buf = reader.readLine()) != null) {
				result += new String(buf.getBytes("utf-8"), "UTF-8") + "\r\n";
			}
		} catch (JSchException | IOException e) {
			result += e.getMessage();
		} finally {
			if (openChannel != null && !openChannel.isClosed()) {
				openChannel.disconnect();
			}
			if (session != null && session.isConnected()) {
				session.disconnect();
			}
		}
		return result;
	}

	public static void main(String args[]) {
		String url = "";
		String userName = "";
		String password = "";
		String port = "22";
		String filePath = "";
		for (int i = 0; i < args.length; i++) {
			try {
				if (args[i].equals("/" + serverUrl) || args[i].equals("-" + serverUrl)) {
					url = args[i + 1];
				} else if (args[i].equals("/" + serverUserName) || args[i].equals("-" + serverUserName)) {
					userName = args[i + 1];
				} else if (args[i].equals("/" + serverPassword) || args[i].equals("-" + serverPassword)) {
					password = args[i + 1];
				} else if (args[i].equals("/" + serverPort) || args[i].equals("-" + serverPort)) {
					port = args[i + 1];
				} else if (args[i].equals("/" + executeFile) || args[i].equals("-" + executeFile)) {
					filePath = args[i + 1];
				}
			} catch (Exception e) {
			}
		}

		url = "192.168.1.22";
		userName = "root";
		password = "yuanben";
		filePath = "E:\\work\\practice\\workspace sts 3.7.2.RELEASE\\sshlogin\\test";
		
		System.out.println(url);
		System.out.println(userName);
		System.out.println(password);
		System.out.println(port);
		System.out.println(filePath);
		if (filePath != null && !filePath.trim().equals("")) {
			StringBuilder command = new StringBuilder();
			try {
				InputStream is = new FileInputStream(filePath);
				byte[] buf = new byte[1024];
				int len = 0;
				while (-1 != (len = is.read(buf))) {
					command.append(new String(buf, 0, len));
				}
				System.out.println(command);
				String exec = exec(url, userName, password, port, "ps -ef|grep tomcat");
				is.close();
				System.out.println(exec);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}