import sys
import os
import time
from PIL import Image
from flask import Flask
from GAN_Fabric import generator

app = Flask(__name__)


@app.route('/low2high/<input>/<output>')
def low2high(input, output):
    print(input)
    print(output)
    input = '/Users/mete0r/Documents/fabric_defect_detect/fabric_defect_detect/server/server/src/main/resources/static/input/' + input
    output = '/Users/mete0r/Documents/fabric_defect_detect/fabric_defect_detect/server/server/src/main/resources/static/output/' + output

    img = Image.open(input)
    w, h = img.size
    if w < 600 and h < 600:
        str = "cd /Users/mete0r/Documents/fabric_defect_detect/fabric_defect_detect/model/generative/highqualitygen;./realesrgan-ncnn-vulkan -i " + input + " -o " + output
        os.system(str)
        time.sleep(5)
        cmd = 'cd /Users/mete0r/Documents/fabric_defect_detect/fabric_defect_detect/model/yolov5;python detect.py --source=' + output
        os.system(cmd)
    else:
        time.sleep(5)
        cmd = 'cd /Users/mete0r/Documents/fabric_defect_detect/fabric_defect_detect/model/yolov5;python detect.py --source=' + input
        os.system(cmd)
    return "success"

@app.route('/gen/<input>')
def generate(input):
    generator(input)
    return 'success'


if __name__ == '__main__':
    app.run()
