package scraper;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class SovScraper
{

	String BASE_URL = "http://dl.eve-files.com/media/corp/verite/";

	int year;
	int month;
	int day;

	public static void main(String[] args)
	{
		SovScraper sovScraper = new SovScraper();
		sovScraper.scrapeImages();
	}

	private void scrapeImages()
	{
		for (year = 2008; year < 2009; year++)
		{
			for (month = 01; month < 13; month++)
			{
				for (day = 01; month < 32; day++)
				{
					URL imageURL = null;
					String uniqueImageCode = String.format("%04d", year) + String.format("%02d", month)
							+ String.format("%02d", day) + ".png";
					try
					{
						imageURL = new URL(BASE_URL + uniqueImageCode);
					} catch (MalformedURLException e2)
					{
						e2.printStackTrace();
						break;
					}

//					InputStream in = null;
//					try
//					{
//						in = new BufferedInputStream(imageURL.openStream());
//					} catch (IOException e1)
//					{
//						e1.printStackTrace();
//						break;
//					}
//
//					ByteArrayOutputStream out = new ByteArrayOutputStream();
//					byte[] buf = new byte[1024];
//					int n = 0;
					try
					{
//						while (-1 != (n = in.read(buf)))
//						{
//							out.write(buf, 0, n);
//						}
//						out.close();
//						in.close();
//						byte[] response = out.toByteArray();
//						InputStream bIn = new ByteArrayInputStream(response);
					
						System.out.println(imageURL);
						BufferedImage sovImage = null;
						sovImage = ImageIO.read(imageURL);

						FileOutputStream fos = new FileOutputStream(
								"C:/Users/Will/Documents/Eclipse/git/EVE SovMap Scraper/Output/" + uniqueImageCode);
						BufferedOutputStream bos = new BufferedOutputStream(fos);
						ImageIO.write(sovImage, "png", bos);
						System.out.println(uniqueImageCode);
						bos.close();
					} catch (IOException e)
					{
						e.printStackTrace();
						break;
					}
				}
			}
		}
}}
