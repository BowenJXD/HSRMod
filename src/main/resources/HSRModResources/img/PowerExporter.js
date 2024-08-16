// 批量导出指定路径下的所有组，并根据原画板大小和缩小后的画板大小分别导出

function main() {
    // 检查是否有打开的文档
    if (app.documents.length === 0) {
        alert("没有打开的文档。");
        return;
    }

    var doc = app.activeDocument;
    var originalWidth = doc.width;
    var originalHeight = doc.height;
    var outputPath = Folder.selectDialog("选择导出文件夹");

    if (outputPath == null) {
        alert("未选择输出文件夹。");
        return;
    }

    var visibleLayers = getInitiallyVisibleLayers(doc);

    if (visibleLayers.length === 0) {
        alert("没有可见的图层组。");
        return;
    }
    
    // 隐藏所有图层组
    hideAllGroups(doc);

    var previousVisibleLayer = null;

    // 第一次导出，使用原画板大小并添加"_p"后缀
    for (var i = 0; i < visibleLayers.length; i++) {
        var layer = visibleLayers[i];

        // 隐藏上一个组（如果存在）
        if (previousVisibleLayer !== null) {
            previousVisibleLayer.visible = false;
        }
        // 显示当前组
        layer.visible = true;

        // 导出当前可见内容，添加"_p"后缀
        saveAsPng(outputPath, layer.name + "Power128");

        // 记录当前组为上一个可见组
        previousVisibleLayer = layer;
    }

    // 将图像缩小
    doc.resizeImage(UnitValue(48, "px"), UnitValue(48, "px"), null, ResampleMethod.BICUBICSHARPER);

    // 第二次导出，使用缩小后的图像大小，不添加后缀
    for (var i = 0; i < visibleLayers.length; i++) {
        var layer = visibleLayers[i];

        // 隐藏上一个组（如果存在）
        if (previousVisibleLayer !== null) {
            previousVisibleLayer.visible = false;
        }
        // 显示当前组
        layer.visible = true;
        // 导出当前可见内容，不添加后缀
        saveAsPng(outputPath, layer.name + "Power48");

        // 记录当前组为上一个可见组
        previousVisibleLayer = layer;
    }

    // 恢复原始图像大小
    doc.resizeImage(UnitValue(originalWidth, "px"), UnitValue(originalHeight, "px"), null, ResampleMethod.BICUBIC);
    
    alert("导出完成！");
}

function getInitiallyVisibleLayers(doc) {
    var visibleLayers = [];
    var layers = doc.layers;

    for (var i = 0; i < layers.length; i++) {
        if (layers[i].visible) {
            visibleLayers.push(layers[i]);
        }
    }
    return visibleLayers;
}

// 隐藏所有图层组
function hideAllGroups(doc) {
    for (var i = 0; i < doc.layers.length; i++) {
        doc.layers[i].visible = false;
    }
}

function showAllGroups(doc) {
    for (var i = 0; i < doc.layers.length; i++) {
        doc.layers[i].visible = true;
    }
}

// 将当前文档保存为PNG格式
function saveAsPng(outputPath, fileName) {
    var file = new File(outputPath + "/" + fileName + ".png");
    var options = new PNGSaveOptions();
    options.compression = 9; // 压缩等级，0（最大速度）到9（最小文件大小）

    app.activeDocument.saveAs(file, options, true, Extension.LOWERCASE);
}

// 运行脚本
main();
