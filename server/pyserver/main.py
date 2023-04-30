from flask import Flask
import os
app = Flask(__name__)

@app.route('/low2high/<input>/<output>')
def low2high(input, output):
    print(input)
    print(output)
    input = '/Users/mete0r/Documents/fabric_defect_detect/fabric_defect_detect/server/server/src/main/resources/static/input/' + input
    output = '/Users/mete0r/Documents/fabric_defect_detect/fabric_defect_detect/server/server/src/main/resources/static/output/' + output
    str = "cd /Users/mete0r/Documents/fabric_defect_detect/fabric_defect_detect/model/generative/highqualitygen;./realesrgan-ncnn-vulkan -i " + input + " -o " + output
    os.system(str)
    return "success"

if __name__ == '__main__':
    app.run()


