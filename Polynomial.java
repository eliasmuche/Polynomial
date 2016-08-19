
/**
 * This class can contain a polynomial 
 * 
 * @author Elias Muche 
 * @version 11/12/15 
 */
public class Polynomial
{
    Term first;
    private int numTerms=0;
  
    
    /**
     * This constructor takes a string representation of a polynomial and creates a new polynomial based off of that string.
     * If the the string conatins a term without an exponent an exception is thrown. 
     * @param poly A string representation of the polynomial
     */
    public Polynomial(String poly){
        if(poly==null){
            throw new IllegalArgumentException("It wasn't a polynomial");//if theres no polynomial
        }
        else if(poly.equals("")){//if the polynomial is empty 
            //create a 0 term
            return;
        }
        double coef=0;
        int exp=0;
        String[] nums=poly.split(" ");//create a list of the terms
        if(nums.length%2==1){//if theres a term with no exponent 
            throw new IllegalStateException("The polynomial must be in coefficient/exponent pairs");

        }
        exp=Integer.parseInt(nums[1]);//get the exponent from the 
        coef=Double.parseDouble(nums[0]);//get the coef
        addTerm(coef,exp);

        if(nums.length>2){//if there are more than 2 terms
            for(int i=2;i<nums.length;i+=2){
                exp=Integer.parseInt(nums[i+1]);//get the exponent 
                coef=Double.parseDouble(nums[i]);//get the coefficient
                addTerm(coef,exp);//and add the term 

            }
        }

    }

    /**
     * This Constructor takes an existing polynomial and creates a deep copy of it.
     * @param p The exsisting polynomial .
     */
    public Polynomial(Polynomial p){

        if(p==null){//if theres no polynomial 
            return;

        }
        else if(p.first==null){//or the first term is nothing 

            return;
        }
        Term terms=p.first;//create a pointer 
        while(terms!=null){
            addTerm(terms.coe,terms.ex);//keep adding terms with the parameters data 
            terms=terms.next;//move the pointer 

        }

    }

    /**
     * This method adds the term to the current polynomial in a way that keeps the decreasing order.
     * @param coef The coefficient of the new term(can't be 0)
     * @param exp The exponent of the new term(if the exponent already exists, like terms are combined)
     */
    public void addTerm(double coef, int exp){

        if(coef==0.0){//if the term is 0
            return;
        }
        else if(exp<0){//if the exponent is negative
            throw new IllegalArgumentException("The exponent must be at least 0"); 
        }
        Term newTerm=null;//initialization
        if(first==null){//if there's no current polynomial
            first=new Term(exp,coef);//create the first term of the polynomial with the parameters as values
            numTerms++;
            return;  
        }
        else if(first.next==null){//if the term is the second item
            if(exp<first.ex){//if the term should placed to the right to keep the order descending
                first.next=new Term(exp,coef);//place the term to the right of the first term
                numTerms++;
            }
            else if(exp>first.ex){//if the term should placed to the left to keep the order descending
                newTerm=new Term(exp,coef);//create a new term with the parameter's as values
                newTerm.next=first;//place it to the left of the first term
                first=newTerm;//update the first term to be the term that was just created
                numTerms++;
            }
            else if(exp==first.ex){//if the exponent is the same as first
                first.coe+=coef;//combine like terms without creating a new term.
                if(first.coe==0.0){//if the combined terms form a zero coefficient 
                    deleteTerm(first.ex);//delete the term             
                }            
                return;                  
            }
            return;
        }
        //At this point there are at least two terms 
        Term pointer=first;//to iterate through the list
        while(pointer.next!=null){//if looking ahead is possible
            if(exp>pointer.ex && pointer==first){//if the term should be placed before the current first term(pointer).
                newTerm= new Term(exp,coef);//create the new term with the parameters as values
                newTerm.next=first;//place the new term to the left of the first term to keep the order descending
                first=newTerm;//update the first term to be the new term
                numTerms++;
                return;
            }
            else if(exp>pointer.next.ex && exp<pointer.ex)//if the term shoudl be placed in between two terms
            {
                newTerm=new Term(exp,coef);//create the new term using the parameters as values
                newTerm.next=pointer.next;//place pointer.next to the right of the newly created term
                pointer.next=newTerm;//place the current term(pointer) to the left of the newly created term
                numTerms++;
                return;
            }

            else if(exp==pointer.ex){//if the exponents match
                pointer.coe+=coef;//combine like terms without creating a new term.
                if(pointer.coe==0.0){
                    deleteTerm(pointer.ex);//if the comb

                }            
                return;               
            }
            pointer=pointer.next;//move the pointer right

        }
        if(exp==pointer.ex){//at this point, the pointer is pointing to the last term
            pointer.coe+=coef;//combine like terms without creating a new term.
            if(pointer.coe==0.0){
                deleteTerm(pointer.ex);

            }
            return;
        }
        else if(exp<pointer.ex){
            newTerm=new Term(exp,coef);//create a new term 
            pointer.next=newTerm;//connect the new term to the end of the current polynomial
            numTerms++;

        }

    }

    /**
     * This method returns an alternate representation of a polynomial.
     * @return An alternate string representation of the polynomial
     */
    public String description(){
        return getDescription(first);//call the recursive method
    }

    private String getDescription(Term term){
        if(first==null){//if there's no polynomial 
            return "0.0";
        }
        else if(first.ex==0){
            return "constant term "+first.coe;

        }
        String descript="";//initialization 

        if(term.next==null){//if the end of the polynomial is reached 

            if(term.ex==0){//and the exponent is 0
                descript+="constant term "+ term.coe+"\n";//attach the coefficient term 

            }
            else{
                descript+="constant "+ term.coe+" exponent "+ term.ex+ "\n";//attach the coefficient term 
            }

        }
        else{

            descript+=getDescription(term.next);//move further down the polynomial
            descript+="constant "+term.coe +" exponent "+term.ex +"\n";//then attach the current term

        }
        return descript;

    }

    /**
     * This method creates a string representation of the polynomial and returns it.
     * @return A string representation of the polynomial
     */
    public String toString(){

        if(first==null){//if there is no polynomial
            return "0.0";

        }
        Term pointer=first;//look at the first term
        String poly="";//initialization 

        //this block of if statements (including the nested ones) handles different situations for the first term
        if(pointer.ex==0){//if the exponent is 0
            poly+=pointer.coe;//only worry about the coefficient
        }

        else if(pointer.coe==1.0||pointer.coe==1){//if the coefficient is 1
            poly+="x";// do not attach the coefficient to x
            if(pointer.ex!=1){//if the exponent isnt 1
                poly+="^"+pointer.ex;//attach an exponent sign
            }

        }
        else if(pointer.coe==-1.0||pointer.coe==-1){//if the coefficient is negative 1
            poly+="-x";//attach a negative sign 
            if(pointer.ex!=1){//if the exponent isnt 1
                poly+="^"+pointer.ex;//attach an exponent sign 
            }

        }
        else{//at this point, the coefficient is not 1

            if(pointer.ex==1){//if the coefficient is 1 
                poly+=pointer.coe+"x";//only attach x
            }
            else{
                poly+=pointer.coe+"x^"+pointer.ex;//otherwise attach x with an exponent sign
            }
        }

        while(pointer.next!=null){
            //at this point, there are at least 2 terms so the different situations for multiple terms are handled here
            pointer=pointer.next;//point to the next term

            if(pointer.ex==0){//if the exponent is 0
                if(pointer.coe<0){//check if the coefficient is negative
                    poly+=" - "+(pointer.coe*(-1));//use a minus sign if it is
                }
                else{
                    poly+=" + "+pointer.coe;//if the coefficient is positive use a plus sign 
                }

            }

            else if(pointer.coe==1.0){//if the coefficient is 1 
                poly+=" + x";//dont attach it to the x
                if(pointer.ex!=1){//if the exponent is not 1
                    poly+="^"+pointer.ex;//attach an exponent sign 
                }

            }
            else if(pointer.coe==-1.0){//if the coefficient is negative 1
                poly+=" - x";//use a minus sign 
                if(pointer.ex!=1){
                    poly+="^"+pointer.ex;//attach an exponent sign 
                }

            }
            else{

                if(pointer.ex==1){//if the exponent is 1
                    if(pointer.coe<0){//check if the coefficient is negative
                        poly+=" - "+((-1)*pointer.coe);//use a minus sign and flip the the sign of the coefficient
                    }
                    else{
                        poly+=" + "+pointer.coe+"x";//otherwise use plus sign 
                    }

                }

                else{//this means the exponent isnt 1 
                    if(pointer.coe<0){//if the coefficient is negative
                        poly+=" - "+(pointer.coe*(-1))+"x^"+pointer.ex;//use a minus sign and flip the sign of the coefficient 

                    }
                    else{
                        poly+=" + "+pointer.coe+"x^"+pointer.ex;//otherwise use a plus sign
                    }

                }
            }

        }
        return poly;
    }

    /**
     * This method returns the number of terms in the polynomial.
     * @return The number of terms
     */
    public int terms(){
        return numTerms;
    }

    /**
     * This method takes an exponent and returns the associated coefficient.
     * If the exponent was invalid, there was no polynomial, or the exponent wasn't in any term, 0.0 is returned
     * @param exp The exponent where the associated coefficient is desired
     * @return The associated coefficient or 0.0 if the parameter was invalid (or there was no such term)
     */
    public double getCoefficient(int exp){
        if(first==null||exp<0){//if theres no polynomial or the parameter is invalid
            System.out.println("There's no such term");
            return 0.0;
        }
        Term pointer=first;//create a pointer that points to the polynomial
        while(pointer!=null){
            if(pointer.ex==exp){//if the exponents match 
                return pointer.coe;//return the associated coefficient

            }
            pointer=pointer.next;//move the pointer
        }
        return 0.0;//return 0.0 if the term wasn't found
    }

    /**
     * Suppose the existing polynomial=f(x), this method takes a number and performs f(number).
     * @param number The number that the polynomial will be evaluated at
     * @return The sum of the terms evaluated at the parameter.
     */
    public double evaluate(double number){
        double sum=0.0;//initialization
        if(first==null){//if there's no polynomial 
            return sum;//do nothing
        }
        else if(first.ex==0){//if theres only one term 
            return first.coe;//return the coefficient  
        }
        else if(first.ex==1){
            sum+=number*first.coe;//if the first term 
        }
        else{
            sum+=Math.pow(number,first.ex)*first.coe;
        }
        Term pointer=first.next;//pointer to iterate through the polynomial 
        while(pointer!=null){
            if(pointer.ex==0){//if the exponent is 0
                sum+=pointer.coe;//add the coefficent
            }
            else if(pointer.ex==1){//if the exponent is 1 
                sum+=number*pointer.coe;//add the product of the parameter and the coefficient 
            }
            else{
                sum+=Math.pow(number,pointer.ex)*pointer.coe;//otherwise add the product of the coefficient and the parameter(raised to the correct power)
            }
            pointer=pointer.next;//iterate 
        }
        return sum;
    }

    /**
     * This method checks if an object is a polynomial with the same terms.
     * @param o The object (possible polynomial) that will be examined
     * @return Whether or not the object was a polynomial with the same terms
     */
    public boolean equals(Object o){
        if(o==null){
            return false;
        }
        else if(!(o instanceof Polynomial)){//if the object isnt a polynomial
            return false;
        }
        Polynomial p=(Polynomial)o;//at this point its an instance of a polynomial so create a pointer
        if(p.first==null){//if theres no polynomial(the parameter)
            if(first==null){//if theres currently no polynomial(this object)
                return true;
            }
            return false;//false because there are terms in this object

        }
        else if(terms()!=p.terms()){//if the number of terms for each polynomial differs
            return false;//dont consider any of the coefficients/exponents

        }
        Term myterm=first;//create a pointer for comparison
        Term term=p.first;//create a pointer for comparison 
        //these if statements compare the first terms
        if(myterm.ex!=term.ex){//if the exponents arent equal 
            return false;
        }
        else{
            if(myterm.coe!=term.coe){//the exponents are the same so compare if the coefficients match 
                return false;
            }

        }

        while(myterm.next!=null){
            myterm=myterm.next;//iterate
            term=term.next;//iterate
            if(myterm.ex!=term.ex){//if the exponents differ
                return false;
            }
            else{
                if(myterm.coe!=term.coe){//the exponents are same so compare the coefficients now
                    return false;
                }

            }

        }
        return true;//at this point the method has failed to return false so the object does equal the parameter
    }

    /**
     * This method will delete a term and return the corresponding coefficient.
     * @param exp The exponent of the term being deleted
     * @return The corresponding coefficient
     */
    public double deleteTerm(int exp){
        double coef=0.0;//initialization
        if(first==null){//if there's no polynomial
            return 0.0;
        }
        else if(terms()==1){//if theres only one term
            if(first.ex==exp){//and the exponents match 
                coef= first.coe;//save the corresponding coefficient
                first=null;//delete the term 
                numTerms--;//update the number of the terms
                return coef;
            }
        }
        else{//this part will delete the first term if necessary 
            if(first.ex==exp){//the number of terms is greater than one so check if the the exponents match
                coef=first.coe;//save the corresponding coefficient 
                first=first.next;//cut off the first term
                numTerms--;//update the number of items 
                return coef;
            }
        }
        Term pointer=first;//create a pointer 
        Term pointer2=pointer.next;//create a pointer one step ahead of the previous pointer
        while(pointer2.next!=null){
            if(pointer2.ex==exp){//if the exponents match
                coef=pointer2.coe;//save the corresponding  coefficient
                pointer.next=pointer2.next;//cut the term out 
                numTerms--;//update the number of terms 
                return coef;
            }    
            pointer=pointer.next;//move the first pointer 
            pointer2=pointer2.next;//move the second pointer
        }
        //this section handles deleting a term at the end if necessary
        if(pointer2.ex==exp){//if the exponents match 
            coef=pointer2.coe;//save the corresponding coefficient 
            pointer.next=null;//cut the term 
            numTerms--;//update the number of terms 
            return coef;
        }
        return 0.0;//at this point the term wasnt found so 0 is returned
    }

    /**
     * This method takes the derivative of the polynomial.
     * @return The derivative of the polynomial
     */
    public Polynomial derivative(){
        Polynomial p=new Polynomial(this);
        Term pointer=p.first;//pointer
        while(pointer!=null){//if theres a term 
            if(pointer.ex==0){//and the term is a constant
                p.deleteTerm(0);//delete it
            }
            else{
                pointer.coe*=pointer.ex;//otherwise use the power rule
                pointer.ex-=1;//power rule
            }
            pointer=pointer.next;//move on to the next term
        }
        return p;
    }

    /**
     * This method takes two polynomials  and returns their sum.
     * @param a The first polynomial
     * @param b The second polynomial 
     * @return The sum of the two polynomials
     */
    public static Polynomial sum(Polynomial a,Polynomial b){
        if(a==null||a.first==null){//if one is empty(no terms or no polynomial)
            return b;//return the other(0 was added)
        }
        else if(b==null||b.first==null){//if the other is emtpy 
            return a;//then return the first one
        }
        Polynomial p=new Polynomial(a);//create a copy of the first polynomial
        Term pointer=b.first;// create a pointer for the second polynomial
        while(pointer!=null){
            p.addTerm(pointer.coe,pointer.ex);//add terms with b's data as parameters
            pointer=pointer.next;//increment 
        }
        return p;
    }

    /**
     * This method takes two polynomials and multiplies them together.
     * @param a The first polynomial 
     * @param b The second polynomial 
     * @return The product of the two polynomials
     */
    public static Polynomial product(Polynomial a, Polynomial b){
        if(a==null||b==null){// if either polynomial is empty 
            return new Polynomial("");
        }
        else if(a.first==null||b.first==null){//or the first term is empty for either pollynomial 
            return new Polynomial("");
        }
        Term pointer=a.first;//first pointer 
        Term pointer2=b.first;//second pointer
        Polynomial p=new Polynomial("");//create an empty polynomial
        while(pointer!=null){//for every term in one polynomial
            while(pointer2!=null){//distribute to every term in the other polynomial
                p.addTerm(pointer.coe*pointer2.coe,pointer.ex+pointer2.ex);//add the powers and multiply the coefficients to create terms in the created polynomial
                pointer2=pointer2.next;//move the second pointer
            }
            pointer=pointer.next;//move the first pointer
            pointer2=b.first;//restart the second pointer
        }
        return p;
    }

    class Term{
        Term next;//next term reference
        int ex;//exponent
        double coe;//coefficient
        
        /**
         * This constructor initializes the coefficient and exponent of the term being created
         * @param ex The desired, exponent 
         * @param coe The desired coefficient
         */
        public Term(int ex,double coe){
            this.ex=ex;//initialize 
            this.coe=coe;//initialize 
        }

    }
}
