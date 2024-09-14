
var doc = app.activeDocument;
var folder = Folder.selectDialog("选择导出文件夹");

if (folder == null) {
    alert("未选择文件夹，脚本终止。");
    exit();
}

var layers = getAllLayers(doc);
var previousLayer = null; // 存储上一个可见的图层
hideAllLayers(layers); // 隐藏所有图层

for (var i = 0; i < layers.length; i++) {
    // 如果存在上一个图层，则隐藏它
    if (previousLayer != null) {
        previousLayer.visible = false;
    }

    // 显示当前图层
    layers[i].visible = true;

    // 获取图层的名称，并清理不合法的文件名字符
    var layerName = layers[i].name.replace(/[\/\\\?\*:|"<>]/g, "_");
    exportLayerAsPNG(folder, layerName);

    // 保存当前图层为上一个图层
    previousLayer = layers[i];
}

// 获取所有图层，包括组内的图层
function getAllLayers(layerSet) {
    var layers = [];
    for (var i = 0; i < layerSet.layers.length; i++) {
        var layer = layerSet.layers[i];
        if (layer.typename == "LayerSet") {
            layers = layers.concat(getAllLayers(layer));
        } else if (layer.visible) {
            layers.push(layer);
        }
    }
    return layers;
}

// 隐藏所有图层
function hideAllLayers(layers) {
    for (var i = 0; i < layers.length; i++) {
        layers[i].visible = false;
    }
}

// 导出图层为PNG
function exportLayerAsPNG(folder, layerName) {
    var file = new File(folder.fsName + "/" + layerName + ".png");

    var pngOptions = new PNGSaveOptions();
    pngOptions.compression = 9;

    var tempDoc = app.activeDocument.duplicate(); // 复制当前文档
    // tempDoc.trim(TrimType.TRANSPARENT); // 去除透明区域

    tempDoc.saveAs(file, pngOptions, true, Extension.LOWERCASE);
    tempDoc.close(SaveOptions.DONOTSAVECHANGES); // 关闭临时文档
}
