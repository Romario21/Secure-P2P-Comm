package com.example.securep2pcomm.security;

import java.util.Arrays;
import java.util.Random;
import java.lang.*;
import java.math.*;

public class RSAcopy {
	public long n;
	public long e;
	private long d;
	private long phiofn;
	private long q;
	private long p;

	public RSAcopy()
	{
		p = largeprime();
		q = largeprime();
		n = p*q;
		phiofn = (p-1)*(q-1);
		e = ecipher(phiofn);
		d = dcipher(e, phiofn);
	/*	System.out.println("p: " + p);
	System.out.println("q: " + q);
	System.out.println("n: " + n);
	System.out.println("phiofn: " + phiofn);
	System.out.println("e: " + e);
	System.out.println("d: " + d);*/
		//*****	String message = "Mario!";
		//	System.out.println("\nMessage: " + message);
		//*****	BigInteger c = new BigInteger("1");
		//*****	String decodedmessage;
		//*****	c = encrypt(message,e,n);
		//	System.out.println("Encrypted Ciphertext: " + c.toString());
		//*****	decodedmessage = decrypt(c,d,n);
		//	System.out.println("DecodedMessage: " + decodedmessage);
	}
	/*public static void main(String[] args){
		RSAcopy theRSA = new RSAcopy();
	}*/

	public long getPk(){
		return this.e;
	}

	public long getPvK(){
		return this.d;
	}

	public long getN(){
		return this.n;
	}

	private long largeprime()
	{
		Random rd = new Random();
		long p = Math.abs(rd.nextLong())%10000000+400000000;
		while(!testprimality(p))
		{
			Random rnext = new Random();
			p = Math.abs(rd.nextLong())%10000000+400000000;/***************************/
		}
		return p;
	}

	private boolean testprimality(long p)
	{
		long remainder;
		int refill = 0;
		long dec;
		int pbinary[]= new int[64];
		Arrays.fill(pbinary,2);
		boolean prime = false;
		int size =0;
		int arr[]= new int[64];
		Arrays.fill(arr,2);
		size = 0;
		dec = p -1;
		long a = 2;
		while(!relativelyprime(dec,a))
		{
			Random rnext = new Random();
			a = Math.abs(rnext.nextLong())%10+2;
		}
		//Converting into binary
		while(dec > 0)
		{
			if(dec%2 == 1)
			{
				arr[size] = 1;
			}
			else
			{
				arr[size] = 0;
			}
			dec = dec/2;
			++size;
		}
		size = size-1;
		refill = 0;
		while(size >= 0)
		{
			pbinary[refill] = arr[size];
			--size;
			++refill;
		}
		//Squaring techinque
		size = 0;
		remainder = 1;
		while(size < refill)
		{
			if(pbinary[size] == 1)
			{
				remainder = (remainder*remainder)%p;
				remainder = remainder * a;
				remainder = remainder%p;
			}
			else if(pbinary[size] == 0)
			{
				remainder = remainder*remainder;
				remainder = remainder%p;
			}
			++size;
		}
		if(remainder == 1)
		{
			prime = true;
		}
		size = 0;
		while(size < refill)
		{
			pbinary[size] = 2;
			arr[size] = 2;
			++size;
		}
		return prime;

	}

	private boolean relativelyprime(long phiofn, long e)
	{
		boolean relprime = false;
		long x = e;
		long y = phiofn;
		long remainder = y%x;
		while(remainder > 0)
		{
			y =x;
			x=remainder;
			remainder = y%x;
		}
		if(x==1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private long ecipher(long phiofn)
	{
		long e = 0;
		long x = 3;
		long y = 6;
		while(!relativelyprime(y,x))
		{

			Random rnext = new Random();
			e = Math.abs(rnext.nextLong()) %(phiofn-2) +2;
			y = phiofn;
			x = e;
		}
		return e;
	}

	private long dcipher(long e, long phiofn)
	{
		int size = 2;
		long v[] = new long[100];
		v[0] = 0;
		v[1] = 1;
		long d = 1;
		long remainder = 1;
		long q = 1;
		long y = phiofn;
		long x = e;
		while(remainder != 0 )
		{
			q = y/x;
			remainder = y-q*x;
			v[size] = v[size-2]-(q*v[size-1]);
			q = 1;
			y=x;
			x=remainder;
			++d;
			++size;
		}
		int run = 0;
		if(v[size-1] > 0)
		{
			v[size] = v[size-2] + v[size-1];
		}
		else
		{
			v[size] = v[size-2] - v[size-1];
		}
		v[size] = v[size]%phiofn;
		d = Math.abs(v[size]);
		return d;
	}


	public BigInteger encrypt(String message, long e,  long n)
	{
		//	System.out.println("E in function: " + e);
		// long p = n;
		BigInteger m_ = new BigInteger(message.getBytes());
		BigInteger e_ = new BigInteger("1");
		BigInteger n_ = new BigInteger("1");
		BigInteger ciphertext = new BigInteger("1");
		//	System.out.println("Message in function: " + m_.toString());
		e_ = e_.valueOf(e);
		n_ = n_.valueOf(n);
		ciphertext = m_.modPow(e_,n_);
		//	  System.out.println("e in function: " + e_.toString());
		//	  System.out.println("n in function: " + n_.toString());
		//	  System.out.println("CIPHER in function: " + ciphertext.toString());
		//System.out.println(e_.toString());
		return ciphertext;
	}
	public String decrypt(BigInteger ciphertext, long d,  long n)
	{
		//	System.out.println("E in function: " + e);
		//	long p = n;
		BigInteger d_ = new BigInteger("1");
		BigInteger n_ = new BigInteger("1");
		BigInteger message = new BigInteger("1");
		//	System.out.println("Ciphertext in function: " + ciphertext.toString());
		d_ = d_.valueOf(d);
		n_ = n_.valueOf(n);
		message = ciphertext.modPow(d_,n_);
		//	System.out.println("d in function: " + d_.toString());
		//	System.out.println("n in function: " + n_.toString());
		//	System.out.println("Decrypted message in function: " + message.toString());
		//System.out.println(e_.toString());
		String finalmessage = new String(message.toByteArray());

		return finalmessage;
	}


}
