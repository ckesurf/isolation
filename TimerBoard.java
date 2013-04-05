import javax.swing.*;
import java.awt.event.*;

public class TimerBoard extends JFrame
{
	private Timer timer;
	public int count = 0;
	
	public TimerBoard()
	{
		timer = new Timer(1000, new TimerListener());
		timer.start();
	}

	private class TimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			count++;
			System.out.println(count + " seconds elapsed");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TimerBoard();
		while(true);
		
	}

}
