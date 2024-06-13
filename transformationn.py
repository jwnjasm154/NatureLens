import os
import random
import shutil

main_folder = "D:/Data50_100"
train_folder = "D:/Train100_(3)/train"
test_folder = "D:/Train100_(3)/test"
validation_folder = "D:/Train100_(3)/val"

train_ratio = 0.8
test_ratio = 0.1
validation_ratio = 0.1

folders = os.listdir(main_folder)
num_progress = len(folders)
for folder in folders:
    print(f'we work in "{folder}" folder \n')

    plant_folder = os.path.join(main_folder, folder)##d:\main\sunflower

    files = os.listdir(plant_folder)

    num_files = len(files)#100
    num_train = int(num_files * train_ratio)#80
    num_test = int(num_files * test_ratio)#10
    num_validation = int(num_files * validation_ratio)#10

    random.shuffle(files)
    train_files = files[:num_train]## [0,1,2,3,4]
    test_files = files[num_train:num_train + num_test]
    validation_files = files[num_train + num_test:num_train + num_test + num_validation]

    if not os.path.exists(os.path.join(train_folder, folder)):
        os.makedirs(os.path.join(train_folder, folder))
    for file in train_files:
        src = os.path.join(plant_folder, file)
        dst = os.path.join(train_folder, folder, file)
        shutil.copyfile(src, dst)
    print(f'"{folder}" train_folder done \n')

    if not os.path.exists(os.path.join(test_folder, folder)):
        os.makedirs(os.path.join(test_folder, folder))

    for file in test_files:
        src = os.path.join(plant_folder, file)#d:/main/sunflower/image1.png
        dst = os.path.join(test_folder, folder, file)##d:/test/sunflower/image1.png
        shutil.copyfile(src, dst)
    print(f'"{folder}" test_folder done \n')

    if not os.path.exists(os.path.join(validation_folder, folder)):
        os.makedirs(os.path.join(validation_folder, folder))

    for file in validation_files:
        src = os.path.join(plant_folder, file)
        dst = os.path.join(validation_folder, folder, file)
        shutil.copyfile(src, dst)
    print(f'"{folder}" validation_folder done \n')

    print(f'remainder {num_progress}')
    num_progress -= 1
# _________________________________________________________________________________ #
# _________________________________________________________________________________ #
#                                                                                   #
#                                                                                   #
#                                                                                   #
#                    Part 2                                                         #
#                                                                                   #
#                                                                                   #
# _________________________________________________________________________________ #
# _________________________________________________________________________________ #
import os
import shutil

source_directory = "D:/Train100_(3)/train"

destination_directory = "D:/Train100_(3)/temp"

folders = next(os.walk(source_directory))[1]

for folder in folders:

    source_folder_path = os.path.join(source_directory, folder)
    destination_folder_path = os.path.join(destination_directory, folder)

    # shutil.move(source_folder_path, destination_folder_path)

    image_folder_path = os.path.join(destination_folder_path, 'imge')

    if not os.path.exists(image_folder_path):
        os.makedirs(image_folder_path)
    # shutil.move(source_folder_path, image_folder_path)

    image_files = os.listdir(os.path.join(source_folder_path))

    for image_file in image_files:
        source_image_path = os.path.join(source_folder_path, image_file)
        destination_image_path = os.path.join(image_folder_path, image_file)
        shutil.move(source_image_path, destination_image_path)

import os
from keras.preprocessing.image import ImageDataGenerator

datagen = ImageDataGenerator(
    # rotation_range=45,
    # width_shift_range=0.2,
    # height_shift_range=0.2,
    # zoom_range=0.2,
    # horizontal_flip=True,
    rescale=1. / 255,
    rotation_range=20,
    width_shift_range=0.2,
    height_shift_range=0.2,
    shear_range=0.2,
    zoom_range=0.2,
    horizontal_flip=True,
    fill_mode='nearest',
    brightness_range=(0.2, 1.5)
)
folders = next(os.walk(destination_directory))[1]
NUM = len(folders)
number = 1
for folder in folders:
    destination_folder_path = os.path.join(destination_directory, folder)
    print(destination_folder_path)
    train_generator = datagen.flow_from_directory(
        destination_folder_path,
        target_size=(224, 224),
        batch_size=62,
        save_to_dir=os.path.join(destination_folder_path, 'imge'),
        save_prefix='processed_image',
        save_format='jpg'
    )
    knum = 25 * 5
    knum1 = 0

    for i in range(1):
        batch = train_generator.next()
        knum1 += 10
        print(f"complet gen {folder} img : {knum1} from {knum} ; complet {number} class from {NUM}")

    print(f"complet {number} class from {NUM}\n")
    number += 1

folders = next(os.walk(destination_directory))[1]
for folder in folders:
    destination_folder_path = os.path.join(destination_directory, folder, 'imge')
    print(destination_folder_path)
    image_files = os.listdir(os.path.join(destination_folder_path))
    image_folder_path = os.path.join(source_directory, folder)

    for image_file in image_files:
        source_image_path = os.path.join(destination_folder_path, image_file)
        destination_image_path = os.path.join(image_folder_path, image_file)
        shutil.move(source_image_path, destination_image_path)
