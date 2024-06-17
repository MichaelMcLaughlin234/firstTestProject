import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;

public class RestAPITest {

	public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
		
		Transcript transcript = new Transcript();
		transcript.setAudio_url("https://github.com/johnmarty3/JavaAPITutorial/raw/main/Thirsty.mp4");
		Gson gson = new Gson();
		String jsonRequest = gson.toJson(transcript);
		
		HttpRequest postRequest = HttpRequest.newBuilder()
				.uri(new URI("https://api.assemblyai.com/v2/transcript"))
				.header("Authorization", "2329b4faab784a51a02c879aa76ac1a5")
				.POST(BodyPublishers.ofString(jsonRequest))
				.build();
		
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpResponse<String> postResponse = httpClient.send(postRequest, BodyHandlers.ofString());
		
		transcript = gson.fromJson(postResponse.body(), Transcript.class);
		
		HttpRequest getRequest = HttpRequest.newBuilder()
				.uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
				.header("Authorization", "2329b4faab784a51a02c879aa76ac1a5")
				.build();
		while (true) {
			HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
			//System.out.println(getResponse);
			
			transcript = gson.fromJson(getResponse.body(), Transcript.class);
			System.out.println(transcript.getStatus());
			
			if ("completed".equals(transcript.getStatus()) || "error".equals(transcript.getStatus())) {
				break;
			}
			Thread.sleep(1000);
		}
		System.out.println(transcript.getText());
	}

}
