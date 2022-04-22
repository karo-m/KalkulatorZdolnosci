import java.util.LinkedList;

public class Kalkulator {

    public int minOkresKredytowania = 6;
    public int maxOkresKredytowania = 100;
    public double maxKwotaKredytu = 150_000;
    public double minKwotaKredytu = 5_000;
    public double maxZaangarzowanie = 200_000;

    public int[] progiDo = {12, 36, 60};

    private LinkedList<String> listaOfert = new LinkedList<>();

    private int okresZatrudnienia;
    private double dochod, KU, ZK, sumaSald;

    private double oprocentowanie;
    private double DTI;
    private double MI;

    private int maxOkresKredytowaniaKlienta;    //w miesiacach
    private double maxMiesiecznaRataKlienta;    //PLN
    private double maxKwotaKredytuKlienta;      //PLN

    public static void main(String[] args) {
        Kalkulator kal = new Kalkulator(args[0], args[1], args[2], args[3], args[4]);
        System.out.println(kal.listaOfert);
    }

    public Kalkulator(String okresZatrudnienia, String dochod, String KU, String ZK, String sumaSald) {
        this.okresZatrudnienia = Integer.parseInt(okresZatrudnienia);
        this.dochod = Math.round(Double.parseDouble(dochod)*100.)/100.;
        this.KU = Math.round(Double.parseDouble(KU)*100.)/100.;
        this.ZK = Math.round(Double.parseDouble(ZK)*100.)/100.;
        this.sumaSald = Math.round(Double.parseDouble(sumaSald)*100.)/100.;

        przedstawOferte();
    }

    public void obliczDTIiOprocentowanie(){

        if(maxOkresKredytowaniaKlienta >= minOkresKredytowania && maxOkresKredytowaniaKlienta <= maxOkresKredytowania){

            if(maxOkresKredytowaniaKlienta <= 12){
                DTI = 0.6;
                oprocentowanie = 0.2;
            }else if(maxOkresKredytowaniaKlienta <= 36){
                DTI = 0.6;
                oprocentowanie = 0.3;
            }else if(maxOkresKredytowaniaKlienta <= 60){
                DTI = 0.5;
                oprocentowanie = 0.3;
            }else{
                DTI = 0.55;
                oprocentowanie = 0.3;
            }
        }

    }

    public void obliczMaxOkresKredytowaniaKlienta(){
        maxOkresKredytowaniaKlienta = Math.min(okresZatrudnienia, maxOkresKredytowania);
    }

    public void obliczMaxMiesRate(){
        maxMiesiecznaRataKlienta = Math.round(Math.min((dochod - KU - ZK), ((DTI * dochod) - ZK)) * 100.) / 100.;
    }

    public void obliczMaxKwoteKredytuKlienta(){
        double[] arr = {maxZaangarzowanie - sumaSald,
                        maxKwotaKredytu,
                        maxMiesiecznaRataKlienta * ((1 - Math.pow((1 + MI), -maxOkresKredytowaniaKlienta)) / MI)};

        maxKwotaKredytuKlienta = Math.round(Math.min( Math.min(arr[0], arr[1]), arr[2]) * 100) / 100.;
    }

    public void przedstawOferte(){
        if(maxOkresKredytowaniaKlienta == 0) {
            obliczMaxOkresKredytowaniaKlienta();
        }else if (maxOkresKredytowaniaKlienta < minOkresKredytowania){
            System.out.println("Brak zdolności kredytowej");
            return;
        }
        obliczDTIiOprocentowanie();
        obliczMaxMiesRate();
        this.MI = oprocentowanie / 12;
        obliczMaxKwoteKredytuKlienta();
        if (maxKwotaKredytuKlienta < minKwotaKredytu){
            System.out.println("Brak zdolności kredytowej");
            return;
        }
        listaOfert.add("\nOferta:\nMaksymalny Okres Kredytowania: " + maxOkresKredytowaniaKlienta + " miesiecy" +
                        "\nMaksymalna miesieczna rata: " + maxMiesiecznaRataKlienta + " PLN" +
                        "\nMaksymalna Kwota Kredytu: " + maxKwotaKredytuKlienta + " PLN");

        // w przypadku gdy maksymalny okres kredytowania pozwala na pokazanie wielu ofert
        if (maxOkresKredytowaniaKlienta <= maxOkresKredytowania && maxOkresKredytowaniaKlienta > progiDo[2]){
            maxOkresKredytowaniaKlienta = progiDo[2];
            przedstawOferte();
        }else if (maxOkresKredytowaniaKlienta > progiDo[1]){
            maxOkresKredytowaniaKlienta = progiDo[1];
            przedstawOferte();
        }else if (maxOkresKredytowaniaKlienta > progiDo[0]){
            maxOkresKredytowaniaKlienta = progiDo[0];
            przedstawOferte();
        }
    }
}
