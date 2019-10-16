package tech.watanave.newyearcard

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.layout.Canvas
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.VerticalAlignment
import java.io.File
import java.io.FileReader

// ハガキサイズ
const val PAGE_WIDTH = 283.465f
const val PAGE_HEIGHT = 419.529f

fun main(args: Array<String>) {
    val pdfWriter = PdfWriter("/tmp/test.pdf")
    val pdfDocument = PdfDocument(pdfWriter)
    pdfDocument.defaultPageSize = PageSize(PAGE_WIDTH, PAGE_HEIGHT)
    val document = Document(pdfDocument)

    val page = pdfDocument.addNewPage()
    val pdfCanvas = PdfCanvas(page)
    val canvas = Canvas(pdfCanvas, pdfDocument, pdfDocument.defaultPageSize)

    val imageResourceUrl = ClassLoader.getSystemResource("sample.png")
    val image = Image(ImageDataFactory.create(imageResourceUrl.file))
    image.setWidth(PAGE_WIDTH)
    image.setHeight(PAGE_HEIGHT)
    canvas.add(image)

    val textResourceUrl = ClassLoader.getSystemResource("sample.txt")
    val textFile = File(textResourceUrl.file)
    val text = FileReader(textFile).readText()
    // "UniJIS-UCS2-H" を "UniJIS-UCS2-V" にすると縦書きに出来るが、座標系がおかしくなるので注意！
    val font = PdfFontFactory.createFont("KozMinPro-Regular", "UniJIS-UCS2-H", false)

    val paragraph = Paragraph(text).setFontSize(15.0f).setFont(font)
    canvas.showTextAligned(paragraph, PAGE_WIDTH / 2, PAGE_HEIGHT / 2, TextAlignment.CENTER, VerticalAlignment.MIDDLE)

    pdfCanvas.release()
    document.close()
    pdfDocument.close()
}