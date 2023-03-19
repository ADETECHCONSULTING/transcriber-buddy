package fr.traore.transcriber.transcriberbuddy;

import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@RestController
public class UploadVideoController {
    Process mProcess;

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            // Déposer le fichier à la racine du dossier "downloads"
            file.transferTo(new File("/Users/adama/Documents/Projects/speech-transcription/downloads/" + fileName));
            runPythonScript("Réunion");
            return ResponseEntity.ok("Fichier " + fileName + " enregistré avec succès.");
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Impossible de sauvegarder le fichier " + fileName + ".");
        }
    }

    private void runPythonScript(String input) throws IOException, InterruptedException {
        Process process;
        try{
            process = Runtime.getRuntime().exec(new String[]{"script_python","arg1","arg2"});
            mProcess = process;
        }catch(Exception e) {
            System.out.println("Exception Raised" + e.toString());
        }
        InputStream stdout = mProcess.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
        String line;
        try{
            while((line = reader.readLine()) != null){
                System.out.println("stdout: "+ line);
            }
        }catch(IOException e){
            System.out.println("Exception in reading output"+ e.toString());
        }
    }
}
