import matplotlib.pyplot as plt
import librosa
import numpy as np
import librosa.display
y, sr = librosa.load('Sample audios/mosquito_1.wav')
##fig, ax = plt.subplots(nrows=3, sharex=True, sharey=True)
##y_harm, y_perc = librosa.effects.hpss(y)
##librosa.display.waveplot(y_harm, sr=sr, alpha=0.25, ax=ax[2])
##librosa.display.waveplot(y_perc, sr=sr, color='r', alpha=0.5, ax=ax[2])
##ax[2].set(title='Harmonic + Percussive')
#
#
##Spectrogram (STFT)
#D = librosa.stft(y)  # STFT of y
##S_db = librosa.amplitude_to_db(np.abs(D), ref=np.max)
#plt.figure()
#librosa.display.specshow(D)
#plt.colorbar()
#
#plt.show()

#import wave
#import numpy as np
#import matplotlib.pyplot as plt
#
##signal_wave = wave.open('Sample audios/background_1.wav', 'r')
#signal_wave = wave.open('Sample audios/mosquito_1.wav', 'r')
#sample_rate = 16000
#sig = np.frombuffer(signal_wave.readframes(sample_rate), dtype=np.int16)
#
#sig = sig[:]
#
##sig = sig[25000:32000]
#
##left, right = data[0::2], data[1::2]
#
#plt.figure(1)
#
#plot_a = plt.subplot(211)
#plot_a.plot(sig)
#plot_a.set_xlabel('sample rate * time')
#plot_a.set_ylabel('energy')
#
#plot_b = plt.subplot(212)
#plot_b.specgram(sig, NFFT=1024, Fs=sample_rate, noverlap=900)
#plot_b.set_xlabel('Time')
#plot_b.set_ylabel('Frequency')

ps = librosa.feature.melspectrogram(y=y, sr=sr)
ps_db= librosa.power_to_db(ps, ref=np.max)
#librosa.display.specshow(ps_db, x_axis='s', y_axis='log')
#fig, ax = plt.figure()
plt.imshow(ps_db, origin="lower", cmap=plt.get_cmap("magma"))

plt.show()


