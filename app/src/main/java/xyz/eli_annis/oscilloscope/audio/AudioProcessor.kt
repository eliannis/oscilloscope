package xyz.eli_annis.oscilloscope.audio

interface AudioProcessor {
    fun process(sample: ShortArray)
}