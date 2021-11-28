import static java.lang.System.out;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Scanner;

public class project2main {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Locale.setDefault(new Locale("en", "US"));
		double diff = 0.000000000001;


		Comparator<Event> myTrainingComparator = new Comparator<Event>() {
			@Override
			public int compare(Event first, Event second) {
				if (!(Math.abs(first.eventTime - second.eventTime) < diff)) {
					if (first.eventTime > second.eventTime)
						return 1;
					else
						return -1;
				}
				else
					return first.player.id - second.player.id;
			}
		};

		Comparator<Event> myPhysiotherapyComparator = new Comparator<Event>() {
			@Override
			public int compare(Event first, Event second) {
				if (!(Math.abs(first.player.trainingTime - second.player.trainingTime) < diff)) {
					if (first.player.trainingTime > second.player.trainingTime)
						return -1;
					else
						return 1;
				}
				else if (!(Math.abs(first.eventTime - second.eventTime) < diff)) {
					if (first.eventTime > second.eventTime)
						return 1;
					else
						return -1;
				}
				else
					return first.player.id - second.player.id;
			}
		};
		Comparator<Event> myMassageComparator = new Comparator<Event>() {
			@Override
			public int compare(Event first, Event second) {
				if (!(Math.abs(first.player.skillLevel - second.player.skillLevel) < diff))
					return second.player.skillLevel - first.player.skillLevel;
				else if (!(Math.abs(first.eventTime - second.eventTime) < diff)){
					if (first.eventTime > second.eventTime)
						return 1;
					else
						return -1;
				}
				else
					return first.player.id - second.player.id;
			}
		};


		PriorityQueue<Event> trainingQueue = new PriorityQueue<>(myTrainingComparator); 
		PriorityQueue<Event> physiotherapyQueue = new PriorityQueue<>(myPhysiotherapyComparator); 
		PriorityQueue<Event> massageQueue = new PriorityQueue<>(myMassageComparator);


		String inputFileName = args[0];
		File myInputFile = new File(inputFileName);
		ArrayList<String[]> myInputArray = new ArrayList<String[]>();
		try {
			Scanner myReader = new Scanner(myInputFile);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String[] splitted = data.split("\\s+");
				myInputArray.add(splitted);
			}
			myReader.close();

		} catch (FileNotFoundException e) {
			out.println("Catch - An error occurred.");
			e.printStackTrace();
		}


		int numOfPlayers = Integer.parseInt(myInputArray.get(0)[0]);
		Player[] players = new Player[numOfPlayers];

		for (int id=0; id<numOfPlayers; id++) {  // puts the players in an array.
			int getSkillLevel = Integer.parseInt(myInputArray.get(id+1)[1]);
			players[id] = new Player(id, getSkillLevel);
		}

		PriorityQueue<Event> events = new PriorityQueue<Event>();
		int numOfArrivals = Integer.parseInt(myInputArray.get(numOfPlayers+1)[0]);
		for (int line=numOfPlayers+2; line<numOfArrivals+numOfPlayers+2; line++) {
			String getType = myInputArray.get(line)[0];
			int getId = Integer.parseInt(myInputArray.get(line)[1]);
			double getEventTime = Double.parseDouble(myInputArray.get(line)[2]);
			double getDuration = Double.parseDouble(myInputArray.get(line)[3]);
			events.add(new Event(getType, players[getId], getEventTime, getDuration));
		}



		PriorityQueue<Physiotherapist> physiotherapists = new PriorityQueue<Physiotherapist>();
		int numOfPhysiotherapist = Integer.parseInt(myInputArray.get(myInputArray.size()-2)[0]);
		for (int phy=0; phy<numOfPhysiotherapist; phy++) {
			int getId = phy;
			double getServiceTime = Double.parseDouble(myInputArray.get(myInputArray.size()-2)[phy+1]);
			physiotherapists.add(new Physiotherapist(getId, getServiceTime));
		}

		PriorityQueue<Coach> coaches = new PriorityQueue<Coach>();
		PriorityQueue<Masseur> masseurs = new PriorityQueue<Masseur>();
		int numOfCoaches = Integer.parseInt(myInputArray.get(myInputArray.size()-1)[0]);
		int numOfMasseur = Integer.parseInt(myInputArray.get(myInputArray.size()-1)[1]);
		for (int coa=0; coa<numOfCoaches; coa++) {
			coaches.add(new Coach(coa));
		}
		for (int mas=0; mas<numOfMasseur; mas++) {
			masseurs.add(new Masseur(mas));
		}




		/*
		 * Begin pulling events from the queue. Time starts.
		 */ 

		double currentTime = 0;

		int maxLenTraining = 0;   ////// FOR OUTPUT 1 Maximum length of the training queue.
		int maxLenPhysiotherapy = 0;   ////// FOR OUTPUT 2 Maximum length of the physiotherapy queue.
		int maxLenMassage = 0;   ////// FOR OUTPUT 3 Maximum length of the massage queue.

		double trainingWaitingSum = 0;    ////// FOR OUTPUT 4 Average waiting time in the training queue.
		double physiotherapyWaitingSum = 0;   ////// FOR OUTPUT 5 Average waiting time in the physiotherapy queue.
		double massageWaitingSum = 0;   ////// FOR OUTPUT 6 Average waiting time in the massage queue.


		double trainingTimeSum = 0;   ////// FOR OUTPUT 7 Average training time.
		double physiotherapyTimeSum = 0;   ////// FOR OUTPUT 8 Average physiotherapy time.
		double massageTimeSum = 0;   ////// FOR OUTPUT 9 Average massage time.

		int numTraining = 0;   ////// FOR OUTPUT 4, 7 Average training time.
		int numPhysiotherapy = 0;   ////// FOR OUTPUT 5, 8 Average physiotherapy time.
		int numMassage = 0;   ////// FOR OUTPUT 6, 9 Average massage time.

		double totalTurnAroundTime = 0; // Total time passed from the training queue entrance until leaving the physiotherapy service


		int totalInvalid = 0;
		int totalCancelled = 0;

		while (events.size() > 0) {

			ArrayList<Event> sameTimeEvents = new ArrayList<Event>();

			sameTimeEvents.add(events.poll());
			currentTime = sameTimeEvents.get(0).eventTime;

			if (!sameTimeEvents.get(0).isValid())
				continue;
			while (events.size() != 0 && (Math.abs(sameTimeEvents.get(0).eventTime-events.peek().eventTime) < diff)
					&& sameTimeEvents.get(0).player.id != events.peek().player.id) {
				sameTimeEvents.add(events.poll());
				if (!sameTimeEvents.get(sameTimeEvents.size()-1).isValid()) {
					sameTimeEvents.remove(sameTimeEvents.get(sameTimeEvents.size()-1));
				}
			}

			for (Event eve : sameTimeEvents) {

				if (eve.type.equals("pe")) {
					eve.player.currentQueue = null;
					eve.player.enterQueueTime = 0;
					eve.player.exitQueueTime = 0;
					physiotherapists.add(new Physiotherapist(eve.player.currentStaff.id, eve.duration));
					eve.player.currentStaff = null;
					eve.player.trainingTime = 0;
				}

				else if (eve.type.equals("tp")) {
					eve.player.currentQueue = physiotherapyQueue;
					eve.player.enterQueueTime = currentTime;
					coaches.add(new Coach(eve.player.currentStaff.id));
					eve.player.currentStaff = null;
					physiotherapyQueue.add(eve);
				}

				else if (eve.type.equals("t")) {
					eve.player.currentQueue = trainingQueue;
					eve.player.enterQueueTime = currentTime;
					trainingQueue.add(eve);
				}

				else if (eve.type.equals("me")) {
					eve.player.currentQueue = null;
					eve.player.enterQueueTime = 0;
					eve.player.exitQueueTime = 0;
					masseurs.add(new Masseur(eve.player.currentStaff.id));
					eve.player.currentStaff = null;
				}

				else if (eve.type.equals("m")) {
					eve.player.currentQueue = massageQueue;
					eve.player.enterQueueTime = currentTime;
					massageQueue.add(eve);
					eve.player.numOfMassage++;
				}
			}
			
			while (masseurs.size() != 0 && massageQueue.size() != 0) {
				Masseur matchMasseur = masseurs.poll();
				Event matchEvent = massageQueue.poll();

				matchEvent.player.currentStaff = matchMasseur;
				matchEvent.player.exitQueueTime = currentTime;

				matchEvent.player.spentTimeMassageQueue += matchEvent.player.exitQueueTime-matchEvent.player.enterQueueTime;

				massageWaitingSum += matchEvent.player.exitQueueTime-matchEvent.player.enterQueueTime;

				massageTimeSum += matchEvent.duration;
				numMassage++;
				
				events.add(new Event("me", matchEvent.player, currentTime+matchEvent.duration, matchEvent.duration));
			}
			if (massageQueue.size()>maxLenMassage)
				maxLenMassage = massageQueue.size();
			

			while (physiotherapists.size() != 0 && physiotherapyQueue.size() != 0) {
				Physiotherapist matchPhysiotherapist = physiotherapists.poll();
				Event matchEvent = physiotherapyQueue.poll();

				matchEvent.player.currentStaff = matchPhysiotherapist;
				matchEvent.player.exitQueueTime = currentTime;

				matchEvent.player.spentTimePhysiotherapyQueue += matchEvent.player.exitQueueTime-matchEvent.player.enterQueueTime;

				physiotherapyWaitingSum += matchEvent.player.exitQueueTime-matchEvent.player.enterQueueTime;

				physiotherapyTimeSum += matchPhysiotherapist.serviceTime;
				numPhysiotherapy++;

				totalTurnAroundTime += (matchEvent.player.exitQueueTime-matchEvent.player.enterQueueTime) + matchPhysiotherapist.serviceTime;

				events.add(new Event("pe", matchEvent.player, currentTime+matchPhysiotherapist.serviceTime, matchPhysiotherapist.serviceTime));
			}
			
			if (physiotherapyQueue.size()>maxLenPhysiotherapy)
				maxLenPhysiotherapy = physiotherapyQueue.size();


			while (coaches.size() != 0 && trainingQueue.size() != 0) {
				Coach matchCoach = coaches.poll();
				Event matchEvent = trainingQueue.poll();

				matchEvent.player.currentStaff = matchCoach;
				matchEvent.player.trainingTime = matchEvent.duration;
				matchEvent.player.exitQueueTime = currentTime;

				trainingWaitingSum += matchEvent.player.exitQueueTime-matchEvent.player.enterQueueTime;

				trainingTimeSum += matchEvent.player.trainingTime;
				numTraining++;

				totalTurnAroundTime += (matchEvent.player.exitQueueTime-matchEvent.player.enterQueueTime) + matchEvent.player.trainingTime;



				events.add(new Event("tp", matchEvent.player, currentTime+matchEvent.duration));
			}
			if (trainingQueue.size()>maxLenTraining)
				maxLenTraining = trainingQueue.size();

		}

		int idOfPhysiotherapy = 0;
		double playerWaitingPhysiotherapy = 0;
		int idOfMassage = -1;
		double playerWaitingMassage = 999999999;

		for (int i = players.length-1; i>-1; i--) {
			totalCancelled += players[i].numOfCancelled;
			totalInvalid += players[i].numOfInvalid;

			if (players[i].spentTimePhysiotherapyQueue - playerWaitingPhysiotherapy > -diff) {
				playerWaitingPhysiotherapy = players[i].spentTimePhysiotherapyQueue;
				idOfPhysiotherapy = players[i].id;
			}

			if (players[i].numOfMassage == 3 && playerWaitingMassage - players[i].spentTimeMassageQueue > -diff) {
				playerWaitingMassage = players[i].spentTimeMassageQueue;
				idOfMassage = players[i].id;
			}
		}
		if (idOfMassage == -1)
			playerWaitingMassage = -1;

		String outputFileName = args[1];   //Writes to output file.
		File myOutputFile = new File(outputFileName);
		try {
			if (myOutputFile.createNewFile()) {
				out.println("File created: " + outputFileName);
			} else {
				out.println("File already exists.");
			}

			FileWriter myWriter = new FileWriter(outputFileName);

			myWriter.write(String.valueOf(maxLenTraining));myWriter.write("\n");
			myWriter.write(String.valueOf(maxLenPhysiotherapy));myWriter.write("\n");
			myWriter.write(String.valueOf(maxLenMassage)); myWriter.write("\n");

			myWriter.write(String.format("%.3f", (double)trainingWaitingSum/(double)numTraining));myWriter.write("\n");
			myWriter.write(String.format("%.3f", (double)physiotherapyWaitingSum/(double)numPhysiotherapy));myWriter.write("\n");
			myWriter.write(String.format("%.3f", (double)massageWaitingSum/(double)numMassage));myWriter.write("\n");

			myWriter.write(String.format("%.3f", (double)trainingTimeSum/(double)numTraining));myWriter.write("\n");
			myWriter.write(String.format("%.3f", (double)physiotherapyTimeSum/(double)numPhysiotherapy));myWriter.write("\n");
			myWriter.write(String.format("%.3f", (double)massageTimeSum/(double)numMassage));myWriter.write("\n");

			myWriter.write(String.format("%.3f", (double)totalTurnAroundTime/(double)numTraining));myWriter.write("\n");

			myWriter.write(String.valueOf(idOfPhysiotherapy)+" ");myWriter.write(String.format("%.3f", playerWaitingPhysiotherapy));myWriter.write("\n");

			myWriter.write(String.valueOf(idOfMassage)+" ");myWriter.write(String.format("%.3f", playerWaitingMassage));myWriter.write("\n");


			myWriter.write(String.valueOf(totalInvalid));myWriter.write("\n");
			myWriter.write(String.valueOf(totalCancelled));myWriter.write("\n");
			myWriter.write(String.format("%.3f", currentTime));myWriter.write("\n");

			myWriter.close();

		} catch (IOException e) {
			out.println("Catch - An error occurred.");
			e.printStackTrace();
		}
	}
}

