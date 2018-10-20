package webcam;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
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

@SuppressWarnings("serial")
public class WebcamPanelWithSubcomponentsExample {

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
