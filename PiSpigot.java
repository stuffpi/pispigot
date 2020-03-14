/*
This is a Java implementation of an algorithm that produces digits of
pi progressively.  Most pi computation algorithms produce nothing
until the computation is completed, but spigot algorithms produce the
first digit quickly and produce additional digits at regular
intervals.

The computation is based on the Wallis formula for pi:

    pi       1   1*2   1*2*3   1*2*3*4
    -- = 1 + - + --- + ----- + ------- + ...
     2       3   3*5   3*5*7   3*5*7*9
    
               1     1*2     1*2*3     1*2*3*4
    pi = 2 + 2*- + 2*--- + 2*----- + 2*------- + ...
               3     3*5     3*5*7     3*5*7*9
    
             1      2      3        4
       = 2 + - (2 + - (2 + - + (2 + - (....
             3      5      7        9


See "A Spigot Algorithm for the Digits of π", by Stanley Rabinowitz
and Stan Wagon.
    http://www.cs.williams.edu/~heeringa/classes/cs135/s15/readings/spigot.pdf
Also described here:
    http://www.cut-the-knot.org/Curriculum/Algorithms/SpigotForPi.shtml

Each digit takes O(n) time to produce (where n is the total number of
digits requested), so the first digit is produced fairly quickly, but
the total runtime is O(n^2). The operation can be made faster by
muliplying by larger powers of 10 and thus producing multiple digits
on each iteration, but the runtime will still be O(n^2).

Here's a C version from Winter and Flammenkamp:

  a[52514],b,c=52514,d,e,f=1e4,g,h;main(){for(;b=c-=14;h=printf("%04d",
  e+d/f))for(e=d%=f;g=--b*2;d/=g)d=d*b+f*(h?a[b]:f/5),a[b]=d%--g;}

This (the Java code below, not the C code above) is free and unencumbered software released into the public domain.

pi_stuff, 2020-03-14
*/

public class PiSpigot {
  private int digits_requested;
  private int[] digits;
  private StringBuilder predigits = new StringBuilder();
  
  // Max value such that digits.length <= INT_MAX.
  //   ceil(((2**31-1) - 1) * 3 / 10)
  private static final int MAX_DIGITS_REQUESTED = 644245094;


  public static void printHelp() {
    System.err.println("\n  PiSpigot [number of digits requested]\n");
  }

  
  public static void main(String args[]) {
    PiSpigot spigot = new PiSpigot();
    if (!spigot.parseArgs(args)) return;
    spigot.run();
  }


  // Get the number of digits requested from the command line arguments.
  public boolean parseArgs(String args[]) {
    if (args.length != 1) {
      printHelp();
      return false;
    }

    try {
      digits_requested = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      printHelp();
      return false;
    }
        
    if (digits_requested <= 0) {
      System.err.println("Digit count must be positive.");
      return false;
    }

    if (digits_requested > MAX_DIGITS_REQUESTED) {
      System.err.println("Maximum digit count is " + MAX_DIGITS_REQUESTED);
      return false;
    }

    return true;
  }


  // Allocate digits[]
  public boolean init() {
    int array_size_needed = digits_requested * 10 / 3 + 1;
    digits = new int[array_size_needed];
    if (digits == null) {
      System.err.printf("Failed to allocate " + (array_size_needed*4)
                        + " bytes.");
      return false;
    }

    // fill each digit with a 2
    for (int i=0; i < digits.length; i++)
      digits[i] = 2;

    return true;
  }
                        

  // Produce digits
  void run() {
    if (!init()) return;

    for (int iter = 0; iter < digits_requested; iter++) {

      // Work backwards through the array, multiplying each digit by 10,
      // carrying the excess and leaving the remainder.
      int carry = 0;
      for (int i=digits.length-1; i > 0; i--) {
        int numerator = i;
        int denomenator = i * 2 + 1;
        int tmp = digits[i] * 10 + carry;
        digits[i] = tmp % denomenator;
        carry = tmp / denomenator * numerator;
      }

      // process the last digit
      int tmp = digits[0] * 10 + carry;
      digits[0] = tmp % 10;
      int digit = tmp / 10;

      // implement buffering and overflow
      if (digit < 9) {
        flushDigits();
        // print a decimal after the leading "3"
        if (iter == 1) System.out.print(".");
        addDigit(digit);
      } else if (digit == 9) {
        addDigit(digit);
      } else {
        overflowDigits();
        flushDigits();
        addDigit(0);
      }
      // System.out.flush();
    }
    flushDigits();
    System.out.println();
  }


  // write the buffered digits
  void flushDigits() {
    System.out.append(predigits);
    predigits.setLength(0);
  }


  // given an integer 0..9, buffer a digit '0' .. '9'
  void addDigit(int digit) {
    predigits.append((char)('0' + digit));
  }


  // add one to each digit, rolling over from from 9 to 0
  void overflowDigits() {
    for (int i=0; i < predigits.length(); i++) {
      char digit = predigits.charAt(i);
      // This could be implemented with a modulo, but compared to the main
      // loop this code is too quick to measure.
      if (digit == '9') {
        predigits.setCharAt(i, '0');
      } else {
        predigits.setCharAt(i, (char)(digit + 1));
      }
    }
  }
        
}
