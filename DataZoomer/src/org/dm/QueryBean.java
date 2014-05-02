package org.dm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.swing.JOptionPane;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

@ManagedBean
@SessionScoped
public class QueryBean {
	private Session session;
	private Channel channel;
	private InputStream is;
	private OutputStream os;
	private PrintStream ps;

	public QueryBean() {
		try {
			JSch jsch = new JSch();

			// jsch.setKnownHosts("/home/foo/.ssh/known_hosts");

			String host = null;
			/*
			 * if (arg.length > 0) { host = arg[0]; } else { host =
			 * JOptionPane.showInputDialog("Enter username@hostname",
			 * System.getProperty("user.name") + "@localhost"); } String user =
			 * host.substring(0, host.indexOf('@')); host =
			 * host.substring(host.indexOf('@') + 1);
			 */
			String user = "user";
			host = "altocumulus.cloud.cs.illinois.edu";
			session = jsch.getSession(user, host, 22);

			String passwd = "password";
			session.setPassword(passwd);

			UserInfo ui = new MyUserInfo() {
				public void showMessage(String message) {
					JOptionPane.showMessageDialog(null, message);
				}

				public boolean promptYesNo(String message) {
					Object[] options = { "yes", "no" };
					int foo = JOptionPane.showOptionDialog(null, message,
							"Warning", JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE, null, options,
							options[0]);
					return foo == 0;
				}

				// If password is not given before the invocation of
				// Session#connect(),
				// implement also following methods,
				// * UserInfo#getPassword(),
				// * UserInfo#promptPassword(String message) and
				// * UIKeyboardInteractive#promptKeyboardInteractive()

			};

			session.setUserInfo(ui);

			// It must not be recommended, but if you want to skip host-key
			// check,
			// invoke following,
			session.setConfig("StrictHostKeyChecking", "no");

			// session.connect();
			session.connect(30000); // making a connection with timeout.

			channel = session.openChannel("shell");
			try {
				channel.connect(5 * 1000);
			} catch (JSchException e1) {
				// // TODO Auto-generated catch block
				e1.printStackTrace();
			}
			is = channel.getInputStream();
			os = channel.getOutputStream();
			ps = new PrintStream(os, true);
			// channel.setOutputStream(System.out);
			// channel.disconnect();
			// Enable agent-forwarding.
			// ((ChannelShell)channel).setAgentForwarding(true);

			/*
			 * // a hack for MS-DOS prompt on Windows.
			 * channel.setInputStream(new FilterInputStream(System.in){ public
			 * int read(byte[] b, int off, int len)throws IOException{ return
			 * in.read(b, off, (len>1024?1024:len)); } });
			 */

			// channel.setOutputStream(System.out);

			/*
			 * // Choose the pty-type "vt102".
			 * ((ChannelShell)channel).setPtyType("vt102");
			 */

			/*
			 * // Set environment variable "LANG" as "ja_JP.eucJP".
			 * ((ChannelShell)channel).setEnv("LANG", "ja_JP.eucJP");
			 */

			// channel.connect();
			// channel.connect(3 * 1000);
		} catch (Exception e) {
			System.out.println("******Shell connection failed");
			System.out.println(e);
		}
	}

	private String queryString;

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String handleCommand(String command, String[] params) {
		// command = command + "\n";
		String outStr = null;
		System.out.println("Executing command: " + command);

		/*
		 * if (channel == null || channel.isClosed() || !channel.isConnected())
		 * { try { channel = session.openChannel("shell"); } catch
		 * (JSchException e2) { // TODO Auto-generated catch block
		 * e2.printStackTrace(); } }
		 */
		System.out.println("Line 150");
		ps.println(command);
		ps.flush();
		System.out.println("Line 153");
		/*
		 * InputStream is = new ByteArrayInputStream(command.getBytes()); //
		 * channel.setInputStream(System.in); channel.setInputStream(is);
		 * System.out.println("****Executing command" + command);
		 * 
		 * OutputStream os = System.out; channel.setOutputStream(System.out); if
		 * (channel == null || channel.isClosed() || !channel.isConnected()) {
		 * try { channel.connect(5 * 1000); } catch (JSchException e1) { // TODO
		 * Auto-generated catch block e1.printStackTrace(); } }
		 */
		byte[] bt = new byte[1024];
        int count=5;
		try {
			outStr = new String();
			System.out.println("Line 167");
			while (true) {

				while (is.available() > 0) {
					System.out.println("Line 169");
					int i = is.read(bt, 0, 1024);
					System.out.println("Line 171 with i:" + i);
					if (i < 0)
						break;
					outStr += new String(bt, 0, i);
					// displays the output of the command executed.
					// System.out.print(str);

				}
				if (channel.isClosed()) {
					break;
				}
			}
			System.out.println("Line 178");

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Line 182 returning string");

		/*
		 * String out=System.out.; try { out = new
		 * String(((ByteArrayOutputStream) os).toByteArray(), "UTF-8"); } catch
		 * (UnsupportedEncodingException e) { e.printStackTrace(); }
		 */
		// System.out.println("****Received output"+out);
		// channel.disconnect();
		return outStr;
		/*
		 * if (command.equals("greet")) { if (params.length > 0) return "Hello "
		 * + params[0]; else return "Hello Stranger"; } else if
		 * (command.equals("date")) return new Date().toString(); else return
		 * command + " not found";
		 */
	}

	public static void main(String[] args) {
		String command = "ls\n";
		InputStream is = new ByteArrayInputStream(command.getBytes());
		QueryBean qb = new QueryBean();
		System.out.println("IS CONNECTED:" + qb.channel.isConnected());
		qb.channel.setInputStream(is);
		System.out.println("****Executing command: " + command);

		OutputStream os = new ByteArrayOutputStream();
		qb.channel.setOutputStream(System.out);
		try {
			qb.channel.connect(3 * 1000);
		} catch (JSchException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*
		 * try { this.wait(1000); } catch (InterruptedException e1) { // TODO
		 * Auto-generated catch block e1.printStackTrace(); }
		 */
		String out = null;
		try {
			out = new String(((ByteArrayOutputStream) os).toByteArray(),
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("****Received output" + out);
		// return out;
		/*
		 * if (command.equals("greet")) { if (params.length > 0) return "Hello "
		 * + params[0]; else return "Hello Stranger"; } else if
		 * (command.equals("date")) return new Date().toString(); else return
		 * command + " not found";
		 */
	}

	public static abstract class MyUserInfo implements UserInfo,
			UIKeyboardInteractive {
		public String getPassword() {
			return null;
		}

		public boolean promptYesNo(String str) {
			return false;
		}

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return false;
		}

		public boolean promptPassword(String message) {
			return false;
		}

		public void showMessage(String message) {
		}

		public String[] promptKeyboardInteractive(String destination,
				String name, String instruction, String[] prompt, boolean[] echo) {
			return null;
		}
	}
}
