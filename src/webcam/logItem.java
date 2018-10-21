package webcam;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;

import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;

@SuppressWarnings("serial")
public class logItem {


	public static void main(String[] args) throws InterruptedException {
		final Dimension size = WebcamResolution.VGA.getSize();

		final Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(size);

		final WebcamPanel panel = new WebcamPanel(webcam, false);
		panel.setFPSDisplayed(true);
		panel.setDisplayDebugInfo(true);
		panel.setImageSizeDisplayed(true);
		panel.setMirrored(true);

		final String play = "PLAY";
		final String stop = "STOP";

		final JButton button = new JButton();
		button.setAction(new AbstractAction(play) {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (panel.isStarted()) {
					panel.stop();
					button.setText(play);
					byte[] bytes = WebcamUtils.getImageBytes(webcam, "jpg");
					analyzeImage(bytes);
					uploadFile(bytes);
				} else {
					panel.start();
					button.setText(stop);
				}
			}
		});

		button.setBounds(getButtonBounds(size));

		panel.setLayout(null);
		panel.add(button);

		final JFrame window = new JFrame("Test webcam panel");
		window.add(panel);
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}

	public static void uploadFile(byte[] bytes) {
		File sourceFile = null, downloadedFile = null;

		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		// Login key credential
		final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=lostitems;"
				+ "AccountKey=13Do7zNke4OsZzsiegA+ERz9AxJtCFaa60+WTi2TtwZTUm"
				+ "jLk8FTMI6zcN3A4L9RJQtmoednr3Iv7fHanOvXUw==;EndpointSuffix=core.windows.net";

		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference("images");

			// Create the container if it does not exist with public access.
			System.out.println("Creating container: " + container.getName());
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
					new OperationContext());

			// Listing contents of container
			for (ListBlobItem blobItem : container.listBlobs()) {
				System.out.println("URI of blob is: " + blobItem.getUri());
			}
		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		try {
			CloudBlockBlob blob = container.getBlockBlobReference("Thao.jpg");
			blob.uploadFromByteArray(bytes, 0, bytes.length);
			System.out.println(blob.getUri());
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (StorageException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void analyzeImage(byte[] bytes) {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Prediction-Key", "a370579678664acaa717341c5c3dd3b4");

		String url = "https://southcentralus.api.cognitive.microsoft.com/customvision/v2.0/Prediction/326a1b1b-49b6-46f9-8a4e-a4c11a0397dd/image";

		Future<HttpResponse<JsonNode>> future = Unirest.post(url).headers(headers).body(bytes)
				.asJsonAsync(new Callback<JsonNode>() {

					public void failed(UnirestException e) {
						// Do something if the request failed
					}

					public void completed(HttpResponse<JsonNode> response) {
						// Do something if the request is successful
						System.out.println(response.getBody());

					}

					public void cancelled() {
						// Do something if the request is cancelled
					}
				});

	}

	private static Rectangle getButtonBounds(Dimension size) {
		final int x = (int) (size.width * 0.1);
		final int y = (int) (size.height * 0.8);
		final int w = (int) (size.width * 0.8);
		final int h = (int) (size.height * 0.1);
		return new Rectangle(x, y, w, h);
	}
}
