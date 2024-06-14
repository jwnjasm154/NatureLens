# NatureLens

Welcome to the NatureLens Application project! This project, developed as part of a graduation requirement, leverages the power of MediaPipe for model training and Kotlin for application development to recognize plants and provide basic information about them.

## Table of Contents
- [Overview](#Overview)
- [Features](#Features)
- [Installation](#Installation)
- [Usage](#Usage)
- [Project Structure](#project-structure)

## Overview

This project involves training a model using MediaPipe to recognize various plants. The trained model is integrated into an Android application developed using Kotlin. The application identifies plants in real-time and displays basic information about the identified plant. 

## Features

- **Plant Recognition**: Identifies plants using a trained model.
- **Information Display**: Provides simple information about the identified plant.

## Installation

### Prerequisites

- Python 3.8+
- Android Studio
- Kotlin

### Setting Up the Environment

1. *Clone the Repository*
    ```bash
    git clone https://github.com/jwnjasm154/NatureLens.git
    cd NatureLens
    ```

2. *Setting Up Python Environment*
     ```bash
    pip install -r Model/requirements.txt
    ```

3. *Setting Up Android Environment*
    - Open the **application** folder in Android Studio.
    - Build the project to download all necessary dependencies.

## Usage

### Training the Model

1. *Run the N_Model.py Script *
    ```bash
    python N_Model.py
    ```

### Running the Application

1. *Deploy to Android Device*
    - Connect your Android device via USB.(or used virtual devices in Android Studio.)
    - In Android Studio, run the application on your device.

## Project Structure

```bash
NatureLens/
├── application/                             # Kotlin code for the Android application
├── Model/                                   
│   ├── Data/                                # Dataset used
│   ├── plant_model_50_100ttv_20/            # trained model
│   ├── N_Model.py                           # Script for training the model
│   ├── transformationn.py                   # Script for generating images from dataset                                      
│   └── requirements.txt                     
├── apk/                                     
│   └── NatureLens.apk                       
├── NatureLens Documention.pdf               # Project documents
├── Presentation.pptx                        # Project Presentation
├── demo_video.mp4                           # Demo video of the application
└── README.md                                
```
---

Feel free to reach out for any questions or feedback regarding the project. Enjoy recognizing plants with our app!
