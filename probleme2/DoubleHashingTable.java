package probleme2;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Random;

public class DoubleHashingTable<AnyType>   {

	
	static int DEFAULT_TABLE_SIZE  = 13;
	static int DEFAULT_R  = 11;
	private int currentSize;
	private int R;
	EntryPair<AnyType>[] items;

	DoubleHashingTable()
	{
		this(DEFAULT_TABLE_SIZE);
	}

	DoubleHashingTable(int size)
	{
		R = DEFAULT_R; 
		allocateArray(size);
		makeEmpty( );
	}

	public void makeEmpty( )
	{
		currentSize = 0;
		for(int i = 0; i < items.length; i++ )
	      items[i] = null;
	}

	/**
	 * Fonction de hashage. Repris des notes de cours de inf2010. Represente H1(x) = X % N
	 * @param x
	 * @return
	 */
	private int myhash(AnyType x)
	{
	   int hashVal = x.hashCode();
	   
	   hashVal %= items.length;
	   
	   if(hashVal < 0)
	      hashVal += items.length;
	   
	   return hashVal;
	}

	/**
	 * Permet d'allouer la m�moire au tableau. Repris des notes de cours de inf2010
	 * @param arraySize
	 */
	@SuppressWarnings("unchecked")
	private void allocateArray(int arraySize)
	{
		items = new EntryPair[nextPrime(arraySize)];
	}
	
	/**
	 * Methode permettant de void si l'element est d�j� pr�sent dans la HashTable. Prend en consid�ration l'association de cl� + valeur
	 * Repris des notes de cours de inf2010
	 * @param x
	 * @return boolean
	 */
	public boolean contains(AnyType x)
	{
	   int currentPos = findPos(x);
	   return isActive(currentPos);
	}
	
	/**
	 * Methode permettant de recuperer un element � partir de sa cl�
	 * @param x
	 * @return
	 */
	public AnyType get(AnyType x)
	{
	   int currentPos = findPos(x);
	   if (items[currentPos] != null)
		   return items[currentPos].element;
	   return null;
	}

	/**
	 * Methode permettant de recuperer le nombre d'�lement pr�sent dans la hashtable
	 * @return int
	 */
    public int nbElement()
    {
        return currentSize;
    }
	/**
	 * Methode permettant de trouver la position d'un objet. Repris des notes de cours de inf2010. 
	 * On modifie legerement la version donn�e dans les notes de cours pour calculer la position comme pr�cis� dans l'�nonc� du TP.
	 * currentPos = (H1 (x) + i H2 (x)) % N 
	 * H1 (x) = x % items.length
	 * H2 (x) = R � (X % R)
	 * @param x
	 * @return int
	 */
	private int findPos(AnyType x)
	{
	   int offset = 0;
	   int currentPos = myhash(x);
			
	   while(items[currentPos] != null && !items[currentPos].element.equals(x))
	   {
	      currentPos = (x.hashCode() % items.length) + (offset++ *  (R - (x.hashCode() % R) )) ;  //currentPos =  H1(x) + i H2(x) 
	      if( currentPos >= items.length )
	         currentPos -= items.length;
	   }
	   return currentPos;
	}

	/**
	 * Methode permettant d'ins�rer un element. Repris des notes de cours de inf2010
	 * @param x
	 */
	public void insert(AnyType x)
	{
	   int currentPos = findPos(x);
	   
	   if(isActive( currentPos ))
	      return;
	   items[currentPos] = new EntryPair<AnyType>(x, true);
	   
	   if(++currentSize > items.length / 2)
	      rehash();
	}

	/**
	 * Methode permettant de supprimer un element. Repris des notes de cours de inf2010
	 * 
	 * @param x
	 */
	public void remove(AnyType x)
	{
	   int currentPos = findPos(x);
	   
	   if(isActive(currentPos))
	      items[currentPos].isActive = false;
	}

	/**
	 * 
	 * Methode permettant de voir si un element est actif. RRepris des notes de cours de inf2010
	 * 
	 * @param currentPos
	 * @return
	 */
	private boolean isActive(int currentPos)
	{
	   return items[currentPos] != null && items[currentPos].isActive;
	}

	/**
	 * Methode permettant de faire le rehash. On reprend la fonction pr�sent� dans les notes de cours de inf2010, en rajoutant R
	 */
	private void rehash( )
	{
		R = items.length;  // On reprend la valeur de l'ancienne valeur de N, qui est forc�ment en nombre premier
		EntryPair<AnyType>[] oldArray = items;
		
		allocateArray(nextPrime(2 * oldArray.length));
		currentSize = 0;

	   for(int i = 0; i < oldArray.length; i++)
	      if(oldArray[i] != null && oldArray[i].isActive)
	         insert(oldArray[i].element);
	}
	
	/**
	 * Methode fourni dans le cours 3 de Inf2010. Permet de valider si un nombre est premier ou non.
	 * @param n
	 * @return boolean
	 */
	private static boolean isPrime(int n)
	{
	   if(n == 2 || n == 3)
	      return true;

	   if(n == 1 || n % 2 == 0)
	      return false;

	   for(int i = 3; i * i <= n; i += 2 )
	      if(n % i == 0)
	         return false;

	   return true;
	}

	/**
	 * Methode fourni dans le cours 3 de Inf2010. Permet de trouver le prochain nombre premier.
	 * @param n
	 * @return int
	 */
	private static int nextPrime(int n)
	{
	   if(n % 2 == 0)
	      n++;

	   for( ; !isPrime( n ); n += 2);
	   return n;
	}
	
	
	/**
	 * 
	 * @author benjamin
	 *
	 *Represente les entr�e du tableau
	 *
	 * @param <AnyType>
	 */
	private static class EntryPair<AnyType>
    {
        public AnyType  element;   // Element en AnyType
        public boolean isActive;  // permet de voir si l'element est actif

       /**
        * Constructeur
        * @param element
        */
		@SuppressWarnings("unused")
		public EntryPair(AnyType element)
        {
            this(element, true);
        }

		/**
		 * Constructeur
		 * @param element
		 * @param active
		 */
        public EntryPair(AnyType element, boolean active)
        {
            this.element  = element;
            isActive = active;
        }
    }
	
	
	
	
}
