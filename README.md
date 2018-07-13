# merkle-stamp
Toolkit for verifying consistency and integrity of assets

## Applications include

### Event stream integrity

Source sends X events, Sink receives Y events

Check that X == Y and events are not lost in transit

**Stamp and digitally sign ..**


### Database integrity

1) Database in state X, code release happens, code rollback happens, database in state Y

Check that X == Y and rollback was a success

2) Database in state X on environment A, release code to environment B, database in state Y

Check that X == Y and code release was a success


### Directory integrity

Filesystem directory in state X on day 1, Filesystem directory in state Y on day 2

Check that X == Y and directory has not been corrupted

#### Building

mvn clean package -Pfile

#### Running

Location of the directory and the SHA hash type to use

<pre> 
~/merkle-stamp$ java -jar target/merkle-stamp-0.0.1-SNAPSHOT.jar /home/pt SHA1

File Stamp      -> 0b42ed1d03044f7c481498ff6d9f23f033f8c6cf
Hash used       -> SHA1
Files processed -> 441884
Time taken secs -> 74
</pre>

### Webserver integrity

Webserver is serving n assets in state X on day 1, Webserver is serving n assets in state Y on day 2

Check that X == Y and assets are not corrupted


### Stamp Server

Coming soon ...


### Attribution

* BouncyCastle
* Jetty
* Jersey
* HdrHistogram

