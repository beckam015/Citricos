package com.uis.simon.citricos.rest;

public class modeloREST {

	
	
	public void Simulacion(int pasosimu) {
		//Declaracion para el while
		int ti=0; //tiempo inicial
		int tf=12; //tiempo final
		double pasoSimu=pasosimu; //Paso de simualción
		
		//Declaracion de parametros
		//1 Frontera Aire-Suelo
		//2-Suelo
		int VariableTemporal=1;
		double RelaInfiltracion=0.85;
		double InclinacionTerre=0;
		//3-Evaporación del cultivo
		double TempMedia=26.8;
		double tmin=23.0;
		double tmax=31.6;
		double RadSolarExtraTer=18.5;
		double Kc=0.65;
		//4-Coeficiente de estres hidrico
		//5-Saturacion del horizonte
		int Anchodelhorizont=150;
		int Largodelhorizon=150;
		int Profundidaddelho=80;
		double PesoVolumetricoS=1.65;
		int PesEspAgua=1;
		double Gravedadespecifi=2.7;
		//6-Punto de marchitez permanente
		int DensidadAgua=1;
		int RelacionPMP=4;
		//7-Capacidad de campo
		int RelacionCC=8;
		//8-Estres hidrico
		//9-Efecto estres hidrico-Brotacion
		//10-Elementos mayores
		int MaximoNAdmitido=1000;
		int MaxKAdmit=600;
		int MaxPAdmit=65;
		//11-Relacion de elementos mayores
		int NormdeSalida=100;
		//12-Dinamica brotes frutos
		double TasadeBrotación=0.0032;
		int PerdBrotes=0;
		int VarTempBrotaFrut=210;
		int PerdFrutos=0;
		
		/*--------------------------------*/
		//Declaracion no linealidades
		//1-frontera
		double Riego=1;
		double Lluvia=1;
		//2-suelo
		double FlujoSubsuperEnt=1;
		//9-Efecto estres
		double Tabla_1=1;
		double Tabla_2=1;
		//10 Elementos mayores
		double NAagregado=1;
		double KAgregado=1;
		double PAgregado=1;
		//11-Relacion de elementos mayores
		double FIS_1=1;
		/*--------------------------------*/
		
		//Declaracion Niveles
		//1 Frontera Aire-Suelo
		double FronteraAirTierr=0;
		double HorizontedeSuelo=0;
		//10-Elementos mayores
		double Nitrogeno=1000;
		double Potasio=600;
		double Fosforo=65;
		//12-Dinamica brotes frutos
		double MaxFloracion=1000;
		double BrotesPosPerd=0;
		double Brotes=0;
		double FrutosPosPerd=0;
		double Frutos=0;
		
		//Declaracion flujos
		//1-Frontera aire-suelo
		double FlujoHidrico;
		double Escorrentia;
		//2-Suelo
		double PercolacionA;
		double EvapoTanspira;
		double FluSubSuperA;
		double PercolacionProfu;
		double FSubsuperficialA;
		//10- Elementeos mayores
		double IngresodeN;
		double IngresodeK;
		double IngresodeP;
		double FlujoAbsN;
		double FlujoAbsK;
		double FlujoAbsP;
		
		//12-Dinamica Brotes Frutos
		double Brotacion;
		double Flujo_1;
		double Fructificacion;
		double Flujo_2;
		double PBrotes;
		double PFrutos;
		
		
		//Declaracion de variables auxiliares
		//1-Frontera aire-suelo
		double Coeficiendeescor=1;
		double ConversionaLitro;
		//2-Suelo
		double RelacionPendFluj=(InclinacionTerre*0.5)/45;
		double VolumenAcumulado;
		//5-Saturacion del horizonte
		double Area=Anchodelhorizont*Largodelhorizon*(0.0001);
		double Volumenfasesolid=Anchodelhorizont*Profundidaddelho*Largodelhorizon;
		double Masasueloseco=(Volumenfasesolid*0.000001)*PesoVolumetricoS;
		double VolumendeSolidos=Masasueloseco/Gravedadespecifi*PesEspAgua;
		double VolumenSaturacio=1000*(Volumenfasesolid*0.000001-VolumendeSolidos);
		//6-punto de marchitez permanente
		double MasadelPMP=(RelacionPMP*(Masasueloseco*1000))/100;
		double VolumenPMP=MasadelPMP/DensidadAgua;
		//7-Capacidad de campo
		double MasadeCCHorA=(RelacionCC*(Masasueloseco*1000))/100;
		double VolumenCapadCamp=MasadeCCHorA/DensidadAgua;
		//4-Coeficiente de estres hidrico
		double Volfacilmentedis=(VolumenCapadCamp+VolumenPMP)*0.5;
		double Ks;
		//3-Evaportranspiracion del cultivo
		double ET0=0.0023*(TempMedia+17.78)*RadSolarExtraTer*(Math.sqrt(Math.abs(tmax-tmin)));
		double ETC=(Kc*ET0)*Area;
		double ETCaj;
		//8-Estres Hidrico
		double CWSI;
		//9-Efecto estres hidrico-brotacion
		double Promedio5dias;
		double Promedio15dias;
		double Resta;
		//10 Elementos mayores
		double AbsorcionN;
		double NNorm;
		double AbsorcionK;
		double KNorm;
		double AbsorcionP;
		double PNorm;
		double NumFrutuniTemp;
		//11 Relacion de elementos mayores
		double SalNormalizada;
		
		
		while(ti<=tf) {
			
			//variables auxiliares
			//1-frontera aire suelo
			ConversionaLitro=(Lluvia)*Area;
			//2-Suelo
			VolumenAcumulado=FronteraAirTierr+HorizontedeSuelo-VolumenSaturacio;
			//Fljos frontera aire suelo
			if(VolumenAcumulado>0) {
				if(FronteraAirTierr>VolumenAcumulado) {
					Escorrentia=(Coeficiendeescor/VariableTemporal)*VolumenAcumulado;
				}else {
					Escorrentia=(Coeficiendeescor/VariableTemporal)*FronteraAirTierr;
				}
			}else {
				Escorrentia=0;
			}
			FlujoHidrico=ConversionaLitro+Riego;
			
			
			//4-Coeficiente de estres hidrico
			double aux1=((VolumenCapadCamp-VolumenPMP)-(VolumenCapadCamp-HorizontedeSuelo))/((VolumenCapadCamp-VolumenPMP)-(VolumenCapadCamp-Volfacilmentedis));
			if(aux1>1) {
				Ks=1;
			}else {
				Ks=aux1;
			}
			//3-Evapotranspiracion del cultivo
			ETCaj=ETC*Ks;
			//Fljos suelo
			if((VolumenSaturacio-HorizontedeSuelo)<FronteraAirTierr) {
				PercolacionA=(1/VariableTemporal)*(VolumenSaturacio-HorizontedeSuelo);
			}else {
				PercolacionA=(1/VariableTemporal)*FronteraAirTierr;
			}
			if(((1/VariableTemporal)*(VolumenSaturacio-HorizontedeSuelo))<FlujoSubsuperEnt) {
				FSubsuperficialA=(1/VariableTemporal)*(VolumenSaturacio-HorizontedeSuelo);
			}else {
				FSubsuperficialA=FlujoSubsuperEnt;
			}
			if(ETCaj<((1/VariableTemporal)*HorizontedeSuelo)) {
				EvapoTanspira=ETCaj;
			}else {
				EvapoTanspira=(1/VariableTemporal)*HorizontedeSuelo;
			}
			if((RelaInfiltracion*RelacionPendFluj*(HorizontedeSuelo-VolumenCapadCamp))>0) {
				FluSubSuperA=(1/VariableTemporal)*RelaInfiltracion*RelacionPendFluj*(HorizontedeSuelo-VolumenCapadCamp);
			}else {
				FluSubSuperA=0;
			}
			if(((1-RelacionPendFluj)*(HorizontedeSuelo-VolumenCapadCamp))>0) {
				PercolacionProfu=RelaInfiltracion*(1/VariableTemporal)*((1-RelacionPendFluj)*(HorizontedeSuelo-VolumenCapadCamp));
			}else {
				PercolacionProfu=RelaInfiltracion*(1/VariableTemporal)*0;
			}
			//8-Estres hidrico
			CWSI=1-(ETCaj/ETC);
			//9-Efecto estres hidrico-brotacion
			
			//Flujos Dinamica Brotes frutos
			PBrotes=(BrotesPosPerd*PerdBrotes)/VariableTemporal;
			Flujo_1=((1-PerdBrotes)*BrotesPosPerd)/VariableTemporal;
			Fructificacion=Brotes/VarTempBrotaFrut;
			PFrutos=PerdFrutos*FrutosPosPerd/VariableTemporal;
			Flujo_2=((1-PerdFrutos)*FrutosPosPerd)/VariableTemporal;
			
			//12 Dinamica Brotes frutos
			NumFrutuniTemp=Fructificacion;
			//10-Elementos mayores
			AbsorcionN=(MaximoNAdmitido/MaxFloracion)*NumFrutuniTemp;
			if(Nitrogeno<MaximoNAdmitido) {
				NNorm=Nitrogeno/MaximoNAdmitido;
			}else {
				NNorm=MaximoNAdmitido/MaximoNAdmitido;
			}
			AbsorcionK=(MaxKAdmit/MaxFloracion)*NumFrutuniTemp;
			if(Potasio<MaxKAdmit) {
				KNorm=Potasio/MaxKAdmit;
			}else {
				KNorm=MaxKAdmit/MaxKAdmit;
			}
			AbsorcionP=(MaxPAdmit/MaxFloracion)*NumFrutuniTemp;
			if(Fosforo<MaxPAdmit) {
				PNorm=Fosforo/MaxPAdmit;
			}else{
				PNorm=MaxPAdmit/MaxPAdmit;
			}
			//11 Relacion de elementos mayores
			SalNormalizada=FIS_1; ///ojo Falta completar
			//Flujos Dinamica Brotes frutos
			Brotacion=((Tabla_1+Tabla_2)*MaxFloracion*SalNormalizada*TasadeBrotación);
			//Niveles
			
			
		}
	}
	
}
