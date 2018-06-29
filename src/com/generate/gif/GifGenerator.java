package com.generate.gif;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Scanner;

public class GifGenerator {

	//gif generator
	public static void gifGenerator(String command)
	{
		try
		{
			CommandLine cmdLine = new CommandLine("cmd.exe");
	        cmdLine.addArgument("cmd /c "+command);
	        DefaultExecutor executor = new DefaultExecutor();
	        executor.setStreamHandler(new PumpStreamHandler(null, null, null));//to stop log view
	        executor.execute(cmdLine);
		}
		catch(ExecuteException e){
			System.err.println(e);
		}
		catch(IOException e) {
			System.err.println(e);
		}
	}
	
	//to get video duration
	public static double getDuration(String file)
	{
		double duration=0.0;
		try
		{
			CommandLine cmdLine = new CommandLine("cmd.exe");
	        cmdLine.addArgument("cmd /c ffprobe -i "+file+" -show_entries format=duration -v quiet -of csv=\"p=0\"");
	        DefaultExecutor executor = new DefaultExecutor();
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
	        executor.setStreamHandler(streamHandler);
	        executor.execute(cmdLine);
	        duration=Double.parseDouble(outputStream.toString());
		}
		catch(ExecuteException e){
			System.err.println(e);
		}
		catch(IOException e) {
			System.err.println(e);
		}
		return duration;
	}
	
	public static boolean isVideoFile(String path) {
	    int n=path.lastIndexOf(".");
	    String extn=path.substring(n+1);
	    if(extn.equalsIgnoreCase("mp4") || extn.equalsIgnoreCase("mkv"))
	    {
	    	return true;
	    }
	    else
	    	 return false;
	}
	//user interaction method
	public static void userInterface()
	{
		String command=null;
		String src=null;
		String dst=null;
		String strt=null;
		String p=null;
		String q=null;
		Double end=3.00;
		double time=0.0;
		int h=0;
		int m=0;
		double s=0.0;
		int count=0;
		double ran=Math.random();
		
		@SuppressWarnings("resource")
		Scanner in=new Scanner(System.in);
		
		//source file
		do {
			if(count>0)
				System.out.println("enter valid file name");
		System.out.println("Enter source video file address(full address)");
		src=in.nextLine();
		count++;
		}while(!(new File(src).exists() && isVideoFile(src)));
		
		//destiny file
		count=0;
		do {
			if(count>0)
				System.out.println("enter valid file name");
		System.out.println("Enter destiny folder address");
		dst=in.nextLine();
		count++;
		}while(!(new File(dst).exists()));
	
		//starting time
		count=0;
		do {
			if(count>0)
				System.out.println("\nEnter valid time:");
		System.out.println("\nEnter starting time:");
		System.out.print("hours:");
			h=in.nextInt();
		System.out.print("Enter minutes:");
			m=in.nextInt();
		System.out.print("Enter seconds:");
			s=in.nextFloat();
			count++;
			time=(3600*h+m*60+s);
		}while(m>=60 || s>=60.0 || time>=getDuration(src));
		
		//ending time
		do {
		System.out.print("\nDo you want the gif to be less than 3 seconds [y/n] :");
		q=in.next();
		}while(!(q.equals("y") || q.equals("Y") || q.equals("n") || q.equals("N")));
		
		if(q.equals("y") || q.equals("Y"))
		{
			System.out.print("\nHow many seconds from the starting point :");
			end=in.nextDouble();
			while(end>3.0)
			{
				System.out.println("\nPlease enter a value less than or equal to 3 :");
				end=in.nextDouble();
			}
		}
	
		strt=h+":"+m+":"+s;
		
		command="ffmpeg -ss "+strt+" -t "+end+" -i "+src+" -filter_complex fps=10,scale=960:-1:flags=lanczos "+dst+"\\output"+ran+".gif";
		gifGenerator(command);
		System.out.println("\noutput file name is : output"+ran+".gif \n you can check the destination ;)");
		System.out.print("\n--------------------------------------------------------------------------------------\n");
		
		do {
		System.out.println("\nDo you want to continue for another gif[y/n]");
		p=in.next();
		}while(!(p.equals("y") || p.equals("Y") || p.equals("n") || p.equals("N")));
		
		if(p.equals("y") || p.equals("Y"))
			userInterface();
		else if(p.equals("n") || p.equals("N"))
		{
			Runtime.getRuntime().exit(1);
		}
	}
	public static void main(String args[])
	{
		userInterface();
	}
}
