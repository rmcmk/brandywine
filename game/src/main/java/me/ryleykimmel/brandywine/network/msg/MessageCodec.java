package me.ryleykimmel.brandywine.network.msg;

public interface MessageCodec<T extends Message> extends MessageEncoder<T>, MessageDecoder<T> {

}
