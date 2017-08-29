package it.seralf.experiments.sheeter

import java.io.FileOutputStream
import java.io.File

object FileUtils {

  def save_file(name: String, content: String, append: Boolean = false) {
    val out_file = new File(name)
    if (!out_file.getParentFile.exists()) out_file.getParentFile.mkdirs()
    val fos = new FileOutputStream(out_file, append)
    fos.write(content.getBytes)
    fos.close()
  }

}