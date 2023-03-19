import os
import math
os.environ["IMAGEIO_FFMPEG_EXE"] = "/opt/homebrew/bin//ffmpeg"
import speech_recognition as sr
import moviepy.editor as mp

# Demander le nom de la vidéo en input
video_name = input("Entrez le nom de la vidéo (sans extension): ")

# Définir le chemin d'accès complet de la vidéo
video_path = os.path.join("downloads", f"{video_name}.mp4")
audio_path = os.path.join("downloads", f"{video_name}.wav")

# Extraire la durée totale de la vidéo
clip = mp.VideoFileClip(video_path)
duration = clip.duration

# Calculer le nombre de segments de 10 Mo maximum nécessaires pour la vidéo
max_segment_size = 10 * 1024 * 1024
num_segments = int(math.ceil(os.path.getsize(video_path) / max_segment_size))

# Découper la vidéo en segments de 10 Mo maximum
for i in range(num_segments):
    segment_start = (i * duration) / num_segments
    segment_end = ((i + 1) * duration) / num_segments
    segment_path = os.path.join("downloads", f"{video_name}_{i}.mp4")
    segment_clip = clip.subclip(segment_start, segment_end)
    segment_clip.write_videofile(segment_path, codec='libx264', audio_codec='aac')

    # Extraire l'audio de chaque segment de vidéo
    segment_audio_path = os.path.join("downloads", f"{video_name}_{i}.wav")
    segment_clip.audio.write_audiofile(segment_audio_path)

    # Transcrire le texte de chaque segment de vidéo
    r = sr.Recognizer()
    with sr.AudioFile(segment_audio_path) as source:
        audio_text = r.recognize_google(r.record(source), language="fr-FR")

    # Écrire le texte transcrit dans un fichier texte
    output_file_path = os.path.join("downloads", f"{video_name}_{i}.txt")
    with open(output_file_path, "w") as file:
        file.write(audio_text)

# Fusionner les fichiers de texte transcrits en un seul fichier
with open(os.path.join("downloads", f"{video_name}.txt"), "wb") as outfile:
    for i in range(num_segments):
        segment_output_path = os.path.join("downloads", f"{video_name}_{i}.txt")
        with open(segment_output_path, "rb") as segment_file:
            outfile.write(segment_file.read())

# Supprimer les fichiers temporaires
for i in range(num_segments):
    os.remove(os.path.join("downloads", f"{video_name}_{i}.mp4"))
    os.remove(os.path.join("downloads", f"{video_name}_{i}.wav"))
    os.remove(os.path.join("downloads", f"{video_name}_{i}.txt"))

print("La transcription est terminée. Le texte transcrit est enregistré dans le fichier :", os.path.join("downloads", f"{video_name}.txt"))
