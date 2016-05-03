Packets are represented by a sequence of bytes broadcast into the network.  The basic format is as follows:

* _Byte index:  Contents_
* 0-1:  16-bit origin client id (little-endian, signed).  Negative values invalid.
* 2-3:  16-bit target client id (little-endian, signed).  A value of -1 indicates a multicast packet to all other clients.
        Other negative values are invalid.  A packet to an unavailable client is ignored.
* 4-5:  16-bit size of contents (little-endian, signed).  Contents length must be between 0 and ??? bytes.
        Negative values invalid.
* 6-X:  Contents.