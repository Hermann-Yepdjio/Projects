# Importing Required Modules & libraries
from tkinter import *
from tkinter.ttk import *
import pygame
import os

DEBUG = True
class MusicPlayer: 
    """One object of this class represents a tkinter GUI application that playsaudio files and can write and read a .m3u playlist."""
    def __init__(self, root): 
        """TODO: Creates a tkinter GUI application that plays audio files and can write and read a .m3u playlist."""
        self.playlistfilename = 'playlist.m3u'
        self.root = root
        self.root.title("Music Player")
        self.root.geometry("1000x200+200+200")
        pygame.init()
        pygame.mixer.init()
        self.track = StringVar()
        self.status = StringVar()
        # Creating trackframe for songtrack label & trackstatus label
        trackframe = LabelFrame(self.root, text="Song Track", relief=GROOVE)
        trackframe.place(x=0, y=0, width=600, height=100)
        songtrack = Label(trackframe, textvariable=self.track).grid(row=0, column=0, padx=10, pady=5)
        trackstatus = Label(trackframe, textvariable=self.status).grid(row=0, column=1, padx=10, pady=5)
        # Creating buttonframe
        buttonframe = LabelFrame(self.root, text="ControlPanel", relief=GROOVE)
        # Inserting song control Buttons
        buttonframe.place(x=0, y=100, width=600, height=100)
        Button(buttonframe, text="Play", command=self.playsong).grid(row=0, column=0, padx=10, pady=5)
        Button(buttonframe, text="Pause", command=self.pausesong).grid(row=0, column=1, padx=10, pady=5)
        Button(buttonframe, text="Unpause", command=self.unpausesong).grid(row=0, column=2, padx=10, pady=5)
        Button(buttonframe, text="Stop", command=self.stopsong).grid(row=0, column=3, padx=10, pady=5)
        # TODO: Insert playlist control Buttons
        Button(buttonframe, text="Load Playlist", command=self.loadplaylist).grid(row=1, column=0, padx=10, pady=5)
        Button(buttonframe, text="Save Playlist", command=self.saveplaylist).grid(row=1, column=1, padx=10, pady=5)
        Button(buttonframe, text="Remove Song", command=self.removesong).grid(row=1, column=2, padx=10, pady=5)
        Button(buttonframe, text="Refresh From Folder", command=self.refresh).grid(row=1, column=3, padx=10, pady=5)
        # Creating songsframe
        songsframe = LabelFrame(self.root, text="Song Playlist", relief=GROOVE)
        songsframe.place(x=600, y=0, width=400, height=200)
        scrol_y = Scrollbar(songsframe, orient=VERTICAL)
        self.playlist = Listbox(songsframe, yscrollcommand=scrol_y.set, selectbackground="gold", selectmode=SINGLE, relief=GROOVE)
        # Applying Scrollbar to playlist Listbox
        scrol_y.pack(side=RIGHT, fill=Y)
        scrol_y.config(command=self.playlist.yview)
        self.playlist.pack(fill=BOTH)
        # Changing directory for fetching songs
        os.chdir("./music")
        # Inserting songs into playlist
        self.refresh()

    def playsong(self): 
        """Displays selected song and its playing status and plays the song."""
        self.track.set(self.playlist.get(ACTIVE))
        self.status.set("-Playing")
        pygame.mixer.music.load(self.playlist.get(ACTIVE))
        pygame.mixer.music.play()
    def stopsong(self): 
        """Displays stopped status and stops the song."""
        self.status.set("-Stopped")
        pygame.mixer.music.stop()
    def pausesong(self): 
        """Displays the paused status and pauses the song."""
        self.status.set("-Paused")
        pygame.mixer.music.pause()
    def unpausesong(self): 
        """Displays the playing status and unpauses the song."""
        self.status.set("-Playing")
        pygame.mixer.music.unpause()
    def removesong(self): 
        """Deletes the active song from the playlist."""
        self.playlist.delete(ACTIVE)
    def loadplaylist(self):
        """
        TODO: Clears the current playlist and loads a previously saved playlist from the music folder. A user firendly message is
        appended to the status if a FileNotFoundError is caught(see the demo video). All other exception messages are
        appended to the status in their default string form.Ignore the lines that start with #."""
        file_name = "playlist.m3u"
        self.playlist.delete(0, END)
        if os.path.isfile(file_name):
            with open(file_name, 'r') as file_content:
                for line in file_content:
                    if (line.endswith(".mp3\n") or line.endswith(".ogg\n") or line.endswith(".wav\n")) and (not line.startswith("#")) and (not line.startswith(".")):
                        self.playlist.insert(END, line[:-1]) #Omitting the last character because it is the new line character (i.e '\n')
        else:
            self.status.set(self.status.get() + " file playlist.m3u was not found.")

    def saveplaylist(self):
        """
        TODO: Save the current playlist to the playlist file in the music folder. All exception messages are appened to the status
        in their default string form. Make sure the first line of the file is only: #EXTM3U"""
        file_name = "playlist.m3u"
        with open(file_name, 'w') as file_content:
            file_content.write("#EXTM3U\n")
            for item in self.playlist.get(0, END):
                file_content.write(item + "\n")

    def refresh(self):
        """
        TODO: Clears the current playlist and fills it with all valid sound files from the music folder. All exception messages are
        appended to the status in their default string form. Insert items into a tkinter Listbox."""
        self.playlist.delete(0, END)
        file_list = os.listdir()
        for f in file_list:
            if (f.endswith(".mp3") or f.endswith(".ogg") or f.endswith(".wav")) and (not f.startswith("#")) and (not f.startswith(".")):
                self.playlist.insert(END, f)

def main(): 
    """Create main window and start a MusicPlayer application on it."""
    # Creating TK root window
    root = Tk()
    # Passing root to the MusicPlayer constructor
    app = MusicPlayer(root)
    # Start the main GUI loop
    root.mainloop()

if __name__ == "__main__":
    main()
