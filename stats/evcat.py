

# historical event era and age categorizer
# kb 6-24-2023

import sys
import csv

defaultErasFile = "eras.csv"

class EventCat:

    def __init__(self, inFile):
        self.inFile = inFile
        self.outFile = ""
        self.willOutput = 0

    def addOutputFile (self, filename):
        self.outFile = filename
        self.willOutput = 1

    def main(self):

        #step1: initialize eras conifg
        FH_config = FileHandler(defaultErasFile)
        tp_eras_ages = FH_config.getEraDict()

        eras = tp_eras_ages[0]
        ages = tp_eras_ages[1]

        FH_dataset = FileHandler(self.inFile)

        eras = FH_dataset.process_eras(eras)
        list_out = []

        for item in eras.items():
            print (item[0], " total : ", str(item[1].occ))
            list_out.append((item[0],item[1].occ))

        if self.willOutput:
            print("Writing to " + self.outFile + "...")
            FW = FileWriter(self.outFile)
            FW.writeCsvFromList(list_out, 'Era')
            print("Done!")


class Era:
    def __init__(self, title, start, end):
        self.title = title
        self.start = start
        self.end = end
        self.occ = 0
    def checkYear(self, year):
        if year >= self.start and year <= self.end:
            return 1
        else:
            return 0

    def incOcc(self):
        #increment the occurrence
        self.occ += 1


class Age:
    def __init__(self, title, start, end):
        self.title = title
        self.start = start
        self.end = end
        self.eras = []
        self.occ = 0

    def addEra(self,inEra):
        self.eras.append(inEra)

    def addEnd(self, inEnd):
        self.end = inEnd

class FileWriter:
    def __init__ (self, filename):
        self.filename = filename

    def writeCsvFromList (self, list_out, period_label):
        with open(self.filename, 'w', newline='') as csvfile:
            fieldnames = [period_label, 'Occurrences']
            writer = csv.DictWriter(csvfile, fieldnames=fieldnames)

            writer.writeheader()
            for item in list_out:
                title = item[0]
                occ = item[1]
                writer.writerow({period_label:title, 'Occurrences': occ})

class FileHandler:

    def __init__(self, filename):
        self.filename  = filename


    def process_eras(self, eras):
        list_eras = eras.values()
        with open(self.filename, newline='') as csvfile:
            event_reader = csv.reader(csvfile, delimiter=',')
            linec = 0
            for row in event_reader:
                if linec == 0: print("Start!")
                else:
                    #now what?
                    year = int(row[0].strip()) #get year of event
                    for each_era in list_eras:
                        if each_era.checkYear(year):
                            #we have an occurrence
                            each_era.incOcc()
                            #uncomment low to debug counting
                            #print(each_era.title + ": " + str(each_era.occ))
                linec+=1
        return eras


    def getEraDict(self):
        eras = {}
        ages = {}

        with open(self.filename, newline='') as csvfile:
            era_reader = csv.DictReader(csvfile)
            linec=0

            for row in era_reader:

                if linec==0:
                    #init age_title_last on first loop
                    age_title_last = ""
                    end_last = -200000000
                    ages[""] = Age("", end_last-1, end_last)
                    #uncomment below to debug column labels/fieldnames
                    #print(f'Column names are {", ".join(row)}')
                    if not('Ages' in row and 'Eras' in row):
                        print("Error! File Headers incorrect!")
                        return
                #grab features
                title = row["Eras"].strip()
                start = int(row["Start"].strip())
                end = int(row["End"].strip())
                #each row is an era
                new_era = Era(title, start, end)
                #add to era dict
                eras[title] = new_era
                #see if we need to add a new age
                age_title = row["Ages"].strip()
                if age_title not in ages:
                    new_age = Age(age_title, start, 0)
                    new_age.addEra(new_era)
                    ages[age_title] = new_age
                    ages[age_title_last].addEnd(end_last)
                else:
                    ages[age_title].eras.append(new_era)

                #end loop by incrementing count
                linec+=1

                #retain age history
                age_title_last = age_title
                end_last = end

                #uncomment to display eras
                #print (new_era.title, " ", new_era.start, " ", new_era.end)
            return (eras, ages)


#region: main executable

if len(sys.argv) == 2:
    print("This will read in " + sys.argv[1] + " then display the output.\nOK?\n")
    response = input("(Y/N) > ")
    #todo: continue this.
    cat = EventCat(sys.argv[1])

elif len(sys.argv) == 3:
    print("This will read in " + sys.argv[1] + " then write the output to the" + sys.argv[2] +" file." "\nOK?\n")
    response = input("(Y/N) > ")
    #todo: continue this.
    cat = EventCat(sys.argv[1])
    cat.addOutputFile(sys.argv[2])
else:
    print("Invalid input. Usage: evcat inputfile ouputfile")
    exit()

#hand it off to the EventCat class

cat.main()

