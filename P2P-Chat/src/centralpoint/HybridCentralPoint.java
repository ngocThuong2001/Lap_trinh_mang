package centralpoint;

import netlib.NetEventListener;
import netlib.Server;
import netlib.PeerInfo;

import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* 
 * localhost: IpV4 preferred
 * port Server 9118
 * */
public class HybridCentralPoint implements NetEventListener
{
	private Server m_server;
	private List m_peers = new LinkedList();

	public HybridCentralPoint() throws IOException
	{
		m_server = new Server(null, 9118, this);
		new Thread(m_server).start();
	}

	@Override
	public boolean handleWrite(SocketChannel ch, int nr_wrote)
	{
		return true;
	}

	@Override
	public boolean handleRead(SocketChannel ch, ByteBuffer buf, int nread)
	{
		try {
			while (buf.hasRemaining()) {
				byte request = buf.get();

				switch (request) {
				case 0x1A: {
					ByteBuffer out = ByteBuffer.allocate(1024);
					out.putInt(m_peers.size());

					Iterator it = m_peers.iterator();
					while (it.hasNext()) {
						PeerInfo info = (PeerInfo) it.next();

						out.put(info.address.getAddress());
						out.putInt(info.port);
					}

					m_server.send(ch, out.array());
					break;
				} case 0x1B: {
					PeerInfo info = new PeerInfo();
					info.port = buf.getInt();
					info.address = ch.socket().getInetAddress();

					Iterator it = m_peers.iterator();
					while (it.hasNext()) {
						PeerInfo i = (PeerInfo) it.next();
						if (i.port == info.port && i.address.equals(info.address))
							return false;
					}

					m_peers.add(info);
					break;
				} default:
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean handleConnection(SocketChannel ch)
	{
		return true;
	}

	@Override
	public boolean handleConnectionClose(SocketChannel ch)
	{
		return true;
	}

	public static void main(String args[])
	{
		try {
			new HybridCentralPoint();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}