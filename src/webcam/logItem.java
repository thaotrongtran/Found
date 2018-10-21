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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.json.JSONArray;
import org.json.JSONObject;

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

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;

@SuppressWarnings("serial")
public class logItem {
	// int itemID = 1000;

//	public logItem() {
//		itemID++;
//	}

	public static void main(String[] args) throws InterruptedException {
		String itemID = UUID.randomUUID().toString();
//		final Dimension size = WebcamResolution.WXGA2.getSize();
//
//		final Webcam webcam = Webcam.getDefault();
//		webcam.setViewSize(size);

		// @formatter:off
		Dimension[] nonStandardResolutions = new Dimension[] { WebcamResolution.PAL.getSize(),
				WebcamResolution.HD.getSize(), new Dimension(2000, 1000), new Dimension(1000, 500), };
		// @formatter:on

		// your camera have to support HD720p to run this code
		Webcam webcam = Webcam.getWebcamByName("HD Webcam C525 1");
		
	
		
		webcam.setCustomViewSizes(nonStandardResolutions);
		webcam.setViewSize(WebcamResolution.HD.getSize());
		webcam.open();

		final WebcamPanel panel = new WebcamPanel(webcam, false);
//		panel.setFPSDisplayed(true);
//		panel.setDisplayDebugInfo(true);
//		panel.setImageSizeDisplayed(true);
//		panel.setMirrored(true);

		final String play = "START LOGGING";
		final String stop = "TAKE A PICTURE";

		final JButton button = new JButton();
		button.setAction(new AbstractAction(play) {
			// int rowKey = 100;
			private AtomicLong idCounter2 = new AtomicLong();

			@Override
			public void actionPerformed(ActionEvent e) {
				if (panel.isStarted()) {
					// panel.stop();
					// button.setText(play);
					byte[] bytes = WebcamUtils.getImageBytes(webcam, "jpg");
					Item temp = new Item(itemID, String.valueOf(idCounter2), bytes);
					// analyzeColor(temp.getBytes());
					String imageURL = uploadFile(temp.getBytes(), itemID.toString() + "-" + idCounter2.toString());
					writeSQL(itemID.toString(), idCounter2.toString(), imageURL);
					analyzeImage(temp.getBytes(), itemID.toString(), idCounter2.toString());
					analyzeColor(temp.getBytes(), itemID.toString(), idCounter2.toString());
					idCounter2.incrementAndGet();
				} else {
					panel.start();
					button.setText(stop);
				}
			}
		});

		button.setBounds(getButtonBounds(WebcamResolution.HD.getSize()));

		panel.setLayout(null);
		panel.add(button);

		final JFrame window = new JFrame("Test webcam panel");
		window.add(panel);
		window.setResizable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}

	public static void writeSQL(String itemID, String imageID, String imageURL) {
		String hostName = "foundgt.database.windows.net";
		String dbName = "foundgt";
		String user = "sqladmin@foundgt";
		// String password = System.getenv("SQL_PASS");
		String password = "F(+AgcD%3a^rzN72";
		String url = String.format(
				"jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
				hostName, dbName, user, password);
		// System.out.println(url);
		Connection connection = null;

		// jdbc:sqlserver://hackgt5.database.windows.net:1433;database=items;user=thaotrongtran@hackgt5;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;

		try {
			connection = DriverManager.getConnection(url);
			String schema = connection.getSchema();
//			System.out.println("Successful connection - Schema: " + schema);
//
//			System.out.println("Query data example:");
//			System.out.println("=========================================");

			// Create and execute a SELECT SQL statement.
			String selectSql = "INSERT INTO Items (itemID, imageID, imageurl) VALUES ('" + itemID + "', '" + imageID
					+ "', '" + imageURL + "');";
			System.out.println(selectSql);
			Statement statement = connection.createStatement();
			statement.executeUpdate(selectSql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeSQLCategory(String category, String itemID, String imageID) {
		String hostName = "foundgt.database.windows.net";
		String dbName = "foundgt";
		String user = "sqladmin@foundgt";
		// String password = System.getenv("SQL_PASS");
		String password = "F(+AgcD%3a^rzN72";
		String url = String.format(
				"jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
				hostName, dbName, user, password);
		// System.out.println(url);
		Connection connection = null;

		try {
			connection = DriverManager.getConnection(url);
			String schema = connection.getSchema();
			// Create and execute a SELECT SQL statement.
			String selectSql = "UPDATE Items SET category = '" + category + "' WHERE itemID = '" + itemID +"' AND imageID ='" + imageID +"';";
			System.out.println(selectSql);
			Statement statement = connection.createStatement();
			statement.executeUpdate(selectSql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String uploadFile(byte[] bytes, String imageID) {

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
//			System.out.println("Creating container: " + container.getName());
//			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
//					new OperationContext());

			// Listing contents of container
//			for (ListBlobItem blobItem : container.listBlobs()) {
//				System.out.println("URI of blob is: " + blobItem.getUri());
//			}
		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		try {
			CloudBlockBlob blob = container.getBlockBlobReference(imageID + ".jpg");
			blob.uploadFromByteArray(bytes, 0, bytes.length);
			return blob.getUri().toString();
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
		return null;
	}

	public static void analyzeImage(byte[] bytes, String a, String b) {
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
						// System.out.println(response.getBody());
						JSONObject myObj = response.getBody().getObject();
						JSONArray arr = myObj.getJSONArray("predictions");
						String res;
						int maxIndex = 0;
						for (int i = 1; i < arr.length(); i++) {
							if (arr.getJSONObject(i).getDouble(
									"probability") > (arr.getJSONObject(maxIndex).getDouble("probability"))) {
								maxIndex = i;
							}
						}
						if (arr.getJSONObject(maxIndex).getDouble("probability") > 0.99) {
							res = arr.getJSONObject(maxIndex).getString("tagName");
							System.out.println(arr.getJSONObject(maxIndex).getDouble("probability"));
						} else {
							res = "Unknown item";
						}
						writeSQLCategory(res, a, b);
					}

					public void cancelled() {
						// Do something if the request is cancelled
					}
				});
	}

	public static void analyzeColor(byte[] bytes, String itemID, String imageID) {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/octet-stream");
		headers.put("Ocp-Apim-Subscription-Key", "366063bf59be41afb6e9c29f8c0cc7fa");

		String url = "https://eastus.api.cognitive.microsoft.com/vision/v2.0/ocr?language=unk&detectOrientation=true";

		Future<HttpResponse<JsonNode>> future = Unirest.post(url).headers(headers).body(bytes)
				.asJsonAsync(new Callback<JsonNode>() {

					public void failed(UnirestException e) {
						// Do something if the request failed
					}

					public void completed(HttpResponse<JsonNode> response) {
						// Do something if the request is successful
						System.out.println("Body of analyze color");
						//System.out.println(response.getBody());
						// JSONObject myObj = response.getBody().getObject();
						String description = "";
						
						JSONObject myObj = response.getBody().getObject();
						//System.out.println(myObj.toString());
						JSONArray arr = myObj.getJSONArray("regions");
						for(int i = 0; i < arr.length(); i++) {
							JSONArray arr2 = arr.getJSONObject(i).getJSONArray("lines");
							//System.out.println(arr2.toString());
							for(int k = 0; k < arr2.length(); k++) {
								JSONArray arr3 = arr2.getJSONObject(k).getJSONArray("words");
								//System.out.println(arr3.toString());
								for(int j = 0; j < arr3.length(); j++) {
									//System.out.println(arr3.getJSONObject(j).getString("text"));
									description += " " + arr3.getJSONObject(j).getString("text");
								}
							}
						}
						writeSQLDescription(description,itemID, imageID);
					}

					public void cancelled() {
						// Do something if the request is cancelled
					}
				});
	}
	
	
	public static void writeSQLDescription(String description, String itemID, String imageID) {
		String hostName = "foundgt.database.windows.net";
		String dbName = "foundgt";
		String user = "sqladmin@foundgt";
		// String password = System.getenv("SQL_PASS");
		String password = "F(+AgcD%3a^rzN72";
		String url = String.format(
				"jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
				hostName, dbName, user, password);
		// System.out.println(url);
		Connection connection = null;

		try {
			connection = DriverManager.getConnection(url);
			String schema = connection.getSchema();
			// Create and execute a SELECT SQL statement.
			String selectSql = "UPDATE Items SET description = '" + description + "' WHERE itemID = '" + itemID +"' AND imageID ='" + imageID +"';";
			System.out.println(selectSql);
			Statement statement = connection.createStatement();
			statement.executeUpdate(selectSql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	private static Rectangle getButtonBounds(Dimension size) {
		final int x = (int) (size.width * 0.1);
		final int y = (int) (size.height * 0.8);
		final int w = (int) (size.width * 0.8);
		final int h = (int) (size.height * 0.1);
		return new Rectangle(x, y, w, h);
	}
}

class Item {
	private String partitionKey, rowKey;
	private byte[] bytes;

	public Item(String partitionKey, String rowKey, byte[] bytes) {
		this.partitionKey = partitionKey;
		this.rowKey = rowKey;
		this.bytes = bytes;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public String getPartitionKey() {
		return partitionKey;
	}

	public String getRowKey() {
		return rowKey;
	}
}
