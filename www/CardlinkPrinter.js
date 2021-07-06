var exec = require('cordova/exec');

exports.testPrint = function(successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'CardlinkPrinter', 'testPrint', []);
};

exports.printPDF = function(base64, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'CardlinkPrinter', 'printPDF', [base64]);
};

exports.printPDFfromPath = function(filePath, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'CardlinkPrinter', 'printPDFfromPath', [filePath]);
};

exports.printBitmap = function(base64, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'CardlinkPrinter', 'printBitmap', [base64]);
};

exports.printText = function(arrayStrings, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'CardlinkPrinter', 'printText', [arrayStrings]);
};

exports.getPrinterStatus = function( successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'CardlinkPrinter', 'getPrinterStatus', []);
};