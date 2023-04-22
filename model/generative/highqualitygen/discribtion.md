# 生成高分辨率图片的模型

1. 此模型使用的是它人提供的开源预训练模型realesrgan，地址 https://github.com/xinntao/Real-ESRGAN
2. 使用时在命令行执行 ./realesrgan-ncnn-vulkan -i input.jpg -o output.png
3. test.png为输入的低分辨率图像，output.png为生成的高清图像resize至2446*1000的结果