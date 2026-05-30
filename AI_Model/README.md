# Crash Detection AI Module

This module contains the machine learning component of the accident detection system.

## Overview

The model uses Google's YAMNet audio embeddings as feature vectors.

Audio clips containing crash sounds and background sounds were processed into embeddings and used to train a binary classifier.

The final model was converted into TensorFlow Lite (.tflite) format for deployment on mobile devices.

## Files

- Crash_Detection_Final.ipynb
  - Training and evaluation notebook

- Example_Spectrograms.ipynb
  - Visualization notebook for audio spectrogram generation

- crash_detector_model.tflite
  - Final deployment model

- X_embeddings.npy
  - Precomputed audio embeddings

- y_labels.npy
  - Labels corresponding to embeddings

- crash_urls.txt
  - Source URLs for crash audio clips

- background_urls.txt
  - Source URLs for background audio clips
