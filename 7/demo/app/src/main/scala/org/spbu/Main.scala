package org.spbu

object Main {
  def main(args: Array[String]): Unit = {
    import LibClass.foo
    println(foo)
  }
}
