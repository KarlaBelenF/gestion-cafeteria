package com.example.cafeteria.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

@Composable
fun CodigoQrReal(contenido: String, modifier: Modifier = Modifier) {
    val bitMatrix = remember(contenido) {
        val writer = QRCodeWriter()
        writer.encode(contenido, BarcodeFormat.QR_CODE, 0, 0)
    }

    Canvas(modifier = modifier) {
        val matrixWidth = bitMatrix.width
        val matrixHeight = bitMatrix.height

        val pixelWidth = size.width / matrixWidth
        val pixelHeight = size.height / matrixHeight
        for (x in 0 until matrixWidth) {
            for (y in 0 until matrixHeight) {
                // Si ZXing dice que hay un punto negro (true), lo pintamos
                if (bitMatrix[x, y]) {
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(x = x * pixelWidth, y = y * pixelHeight),
                        size = Size(width = pixelWidth, height = pixelHeight)
                    )
                }
            }
        }
    }
}