package scraper;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.objectplanet.image.PngEncoder;

public class SovScraper
{

	String BASE_URL = "http://go-dl1.eve-files.com/media/corp/verite/";
	BufferedImage sovImage = null;
	ExecutorService executorService = Executors.newCachedThreadPool();
	URL imageURL = null;
	PngEncoder encoder = new PngEncoder();

	int year;
	int month;
	int day;

	public static void main(String[] args)
	{
		SovScraper sovScraper = new SovScraper();
		try
		{
			sovScraper.scrapeImages();
			sovScraper.waitForImages();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void scrapeImages() throws IOException
	{
		for (year = 2007; year < 2016; year++)
		{
			for (month = 01; month < 13; month++)
			{
				for (day = 01; day < 32; day++)
				{
					String uniqueImageCode = readImageFromUrl();
					if (uniqueImageCode != null)
					{
						writeImage(uniqueImageCode);
					}
				}
			}
		}
	}

	private String readImageFromUrl()
	{
		String uniqueImageCode = String.format("%04d", year) + String.format("%02d", month)
				+ String.format("%02d", day);
		try
		{
			imageURL = new URL(BASE_URL + uniqueImageCode + ".png");
		}
		catch (MalformedURLException e)
		{
			System.out.println(imageURL + " not found");
			return null;
		}

		// System.out.println(imageURL);

		try
		{
			sovImage = ImageIO.read(imageURL);
		}
		catch (IOException e)
		{
			System.out.println(imageURL + " could not read");
			return null;
		}

		return uniqueImageCode;
	}

	private void writeImage(String uniqueImageCode)
	{
		Runnable ioTask = () ->
		{
			try
			{
				FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + "/Output/" + uniqueImageCode + ".png");
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				encoder.encode(sovImage, bos);
				bos.close();
				System.out.println(uniqueImageCode + ".png "
						+ TimeUnit.MILLISECONDS.convert(getCpuTime(), TimeUnit.NANOSECONDS) + "ms");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		};

		executorService.submit(ioTask);
	}

	public void waitForImages()
	{
		executorService.shutdown();

		try
		{
			executorService.awaitTermination(3, TimeUnit.HOURS);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public long getCpuTime()
	{
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		return bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadCpuTime() : 0L;
	}
}
