package org.spbsu

object Main {
  def main(args: Array[String]): Unit = {
    import LibClass.foo
    println(foo)
  }
}
