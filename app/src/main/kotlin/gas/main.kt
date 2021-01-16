package gas

import java.io.*
import kotlin.io.*

fun interface Command{
    fun execute()
}

abstract class ArgsCommand(val args: Array<String>): Command{}

class Buch(vararg command: Command): Command{
    private var items: MutableList<Command> = mutableListOf()
    init{
        for(c in command){
            items.add(c)
        }
    }

    override fun execute(){
        for(c in items){
            c.execute()
        }
    }
}

class ReadCommand(args: Array<String>): ArgsCommand(args){
    override fun execute(){
        readFrom(args[1], args[2]).show()
    }
}

open class MessageCommand(val text: String): Command{
    override fun execute(){
        println(text)
    }
}

class UsageCommand: MessageCommand("usage: fa <command> <param>"){}

class NoSuchCommand(val wrongCommand: String): MessageCommand("No such command: $wrongCommand") {}

fun interface View{
    fun show()
}

open class ContentView(protected val content: String): View{
    override fun show(){
        // @todo: formatting content with 
            // pretty print here or some how
        println(content)
    }
}

class HexView(private val bytes: ByteArray): View{
    override fun show(){
        for(i in bytes.indices){
            val byteValue: Int = bytes[i].toInt() and 0xFF
            if(byteValue < 10){
                print(0)
            }
            print("${byteValue.toString(16)} ")
            if((i + 1) % 16 == 0){
                println()
            }
        }
    }
}
class TextView(bytes: ByteArray): ContentView(bytes.joinToString()){}

fun readFrom(path: String, mode: String): View{
    fun getView(content: ByteArray): View{
        return when(mode){
            "hex" -> HexView(content)
            else -> TextView(content)
        }
    }


    val file: File = File(path)
    return if(file.exists()){
        val stream: InputStream = DataInputStream(FileInputStream(file).buffered())
        val bytes = ByteArray(stream.available())
        stream.read(bytes)
        stream.close()
        getView(bytes)
    }else{
        View{println("path = $path: File not exists!")}
    }
}

fun main(args: Array<String>){
    println("File Analizer tool: version 0.0.0")   
    internalRun(args)
}

fun internalRun(args: Array<String>){
    fun getCommand(): Command{
        return if(args.size > 0){
            when(args[0]){
                "read" -> ReadCommand(args)
                else -> Buch(
                            NoSuchCommand(args[0]),
                            UsageCommand()
                        )
            }    
        }else{
            UsageCommand()
        }    
    }    
    getCommand().execute()
}
