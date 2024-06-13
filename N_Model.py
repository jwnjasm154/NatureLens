import os
import tensorflow as tf
from mediapipe_model_maker.python.core.utils import quantization
assert tf.__version__.startswith('2')
from mediapipe_model_maker import image_classifier
tf.config.list_physical_devices('GPU')

image_train = 'D:/Train100_(3)/train'
image_test= 'D:/Train100_(3)/test'
image_val = 'D:/Train100_(3)/val'

labels = []
for i in os.listdir(image_train):
  if os.path.isdir(os.path.join(image_train, i)):
    labels.append(i)
print(labels)

data_train = image_classifier.Dataset.from_folder(image_train)
data_test = image_classifier.Dataset.from_folder(image_test)
data_val = image_classifier.Dataset.from_folder(image_val)

spec = image_classifier.SupportedModels.EFFICIENTNET_LITE2
hparams = image_classifier.HParams(epochs=50, learning_rate=0.05,export_dir="plant_model_50_100ttv_test")
options = image_classifier.ImageClassifierOptions(supported_model=spec, hparams=hparams)
options.model_options = image_classifier.ModelOptions(dropout_rate = 0.07)
model_2 = image_classifier.ImageClassifier.create(
    train_data = data_train,
    validation_data = data_val,
    options=options,
)
loss, accuracy = model_2.evaluate(data_test)
quantization_config = quantization.QuantizationConfig.for_int8(data_train)
model_2.export_model(model_name="plantM_int8.tflite", quantization_config=quantization_config)



