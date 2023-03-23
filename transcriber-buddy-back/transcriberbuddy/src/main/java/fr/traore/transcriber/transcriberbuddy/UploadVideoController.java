package fr.traore.transcriber.transcriberbuddy;

import org.python.apache.commons.compress.compressors.FileNameUtil;
import org.python.apache.commons.compress.utils.FileNameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

@RestController
public class UploadVideoController {
    Process mProcess;

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            // Déposer le fichier à la racine du dossier "downloads"
            file.transferTo(new File("/Users/adama/Documents/Projects/speech-transcription/downloads/" + fileName));
            runPythonScript(fileName.substring(0, fileName.lastIndexOf('.')));
            return ResponseEntity.ok("Fichier " + fileName + " enregistré avec succès.");
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Impossible de sauvegarder le fichier " + fileName + ".");
        }
    }

    private void runPythonScript(String input) throws IOException, InterruptedException {
        String scriptPath = "/Users/adama/Documents/Projects/speech-transcription/transcription.py";
        String argument = "test-video";

        try {
            String[] command = new String[]{"python3", scriptPath, argument};
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
