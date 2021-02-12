package xyz.eli_annis.oscilloscope.audio

class SampleQueue(size: Int) {

    private var data : ArrayList<Short> = ArrayList()
    private val size = size

    fun contains(element : Short) : Boolean {
        return data.contains(element)
    }

    fun push(element : Short){
        data.add(element)

        if(data.size >= size){
            data.removeAt(0)
        }
    }

    fun pushAll(samples: ShortArray){
        data.addAll(samples.asList())

        if(data.size > size){
            val drop = data.size - size
            data = ArrayList(data.subList(drop, data.size-1))
        }
    }

    fun pop() : Short {
        val element : Short = data.last()
        data.removeAt(data.lastIndex)
        return element
    }

    fun peek() : Short {
        return data.last()
    }

    fun get(index: Int) : Short {
        return data[index]
    }

    fun set(index: Int, element: Short){
        data[index] = element
    }

    fun getData() : ArrayList<Short> {
        return data
    }

    fun getSize() : Int {
        return size
    }
}