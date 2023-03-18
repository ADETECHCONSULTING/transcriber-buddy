import os
os.environ["IMAGEIO_FFMPEG_EXE"] = "/opt/homebrew/bin//ffmpeg"
import speech_recognition as sr
import moviepy.editor as mp

# Demander le nom de la vidéo en input
video_name = input("Entrez le nom de la vidéo (sans extension): ")

# Définir le chemin d'accès complet de la vidéo
video_path = os.path.join("downloads", f"{video_name}.mp4")
audio_path = os.path.join("downloads", f"{video_name}.wav")


# Extraction de l'audio à partir de la vidéo
clip = mp.VideoFileClip(video_path)
clip.audio.write_audiofile(audio_path)


# Vérifier si la vidéo existe
if not os.path.isfile(video_path):
    print("La vidéo n'existe pas")
    exit()

# Initialiser le recognizer
r = sr.Recognizer()

# Ouvrir la vidéo en tant que source audio
with sr.AudioFile(audio_path) as source:
    # Analyser le fichier audio
    audio_text = r.recognize_google(r.record(source), language="fr-FR")

# Écrire le texte transcrit dans un fichier texte
output_file_path = os.path.join("downloads", f"{video_name}.txt")
with open(output_file_path, "w") as file:
    file.write(audio_text)

print("La transcription est terminée. Le texte transcrit est enregistré dans le fichier :", output_file_path)
