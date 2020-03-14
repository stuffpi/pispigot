# pispigot
Spigot algorithm for computing digits of pi

This is a Java implementation of an algorithm that produces digits of
pi progressively.  Most pi computation algorithms produce nothing
until the computation is completed, but spigot algorithms produce the
first digit quickly and produce additional digits at regular
intervals.

The computation is based on the Wallis formula for pi:

    pi       1   1*2   1*2*3         1*2*3*4
    -- = 1 + - + --- + ----- + ... + ------- + ...
     2       3   3*5   3*5*7         3*5*7*9
    
               1     1*2     1*2*3
    pi = 2 + 2*- + 2*--- + 2*----- + ...
               3     3*5     3*5*7
    
             1      2      3        4
       = 2 + - (2 + - (2 + - + (2 + - (....
             3      5      7        9

See ["A Spigot Algorithm for the Digits of π"](http://www.cs.williams.edu/~heeringa/classes/cs135/s15/readings/spigot.pdf), by  Rabinowitz and Wagon, or [this web page](http://www.cut-the-knot.org/Curriculum/Algorithms/SpigotForPi.shtml).

Each digit takes O(n) time to produce (where n is the total number of
digits requested), so the first digit is produced fairly quickly, but
the total runtime is O(n^2). The operation can be made faster by
muliplying by larger powers of 10 and thus producing multiple digits
on each iteration, but the runtime will still be O(n^2).

### To build

    javac PiSpigot.java
or just run "make".

### To run

    java PiSpigot <number of digits to compute>

For example, to produce 100 digits:

    $ java PiSpigot 100
    3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117067

## TL;DR

Here's a C version from Winter and Flammenkamp:

    a[52514],b,c=52514,d,e,f=1e4,g,h;main(){for(;b=c-=14;h=printf("%04d",
    e+d/f))for(e=d%=f;g=--b*2;d/=g)d=d*b+f*(h?a[b]:f/5),a[b]=d%--g;}

@stuffpi, 2020-03-14
