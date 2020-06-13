package org.spbsu

object Main {
  def main(args: Array[String]): Unit = {
    import LibClass.product
    println(s"Imported $product")
  }

}
