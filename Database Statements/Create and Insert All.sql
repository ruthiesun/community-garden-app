CREATE TABLE Soil(
                     Type       VARCHAR(255) PRIMARY KEY,
                     Alkalinity VARCHAR(255) CHECK ( Alkalinity IN ('ACIDIC', 'ALKALINE', 'NEUTRAL')),
                     Aeration   VARCHAR(255) CHECK ( Aeration IN ('HIGH', 'MEDIUM', 'LOW'))
);

CREATE TABLE Fertilizer(
                           FertilizerID INTEGER GENERATED ALWAYS as IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
                           Name VARCHAR(255) NOT NULL,
                           NPKRatio VARCHAR(255),
                           Type VARCHAR(255),
                           Description VARCHAR(1000));

CREATE TABLE Plant(
                      Name VARCHAR(255) PRIMARY KEY,
                      SowingStartDate NUMBER(2,0) check (SowingStartDate >= 0),
                      SowingEndDate NUMBER(2,0) check (SowingEndDate >= 0),
                      PlantStartDate NUMBER(2,0) check (PlantStartDate >= 0),
                      PlantEndDate NUMBER(2,0) check (PlantEndDate >= 0),
                      HarvestStartDate NUMBER(2,0) check (HarvestStartDate >= 0),
                      HarvestEndDate NUMBER(2,0) check (HarvestEndDate >= 0),
                      DormancyStartDate NUMBER(2,0) check (DormancyStartDate >= 0),
                      DormancyEndDate NUMBER(2,0) check (DormancyEndDate >= 0),
                      Water VARCHAR(1000),
                      Shade VARCHAR(1000),
                      Fertilization VARCHAR(1000));

CREATE TABLE Annual(
                       Name VARCHAR(255) PRIMARY KEY,
                       FOREIGN KEY(Name) REFERENCES Plant
                           ON DELETE CASCADE
);

CREATE TABLE Perennial(
                          Name VARCHAR(255) PRIMARY KEY,
                          PruningDate NUMBER,
                          LifespanYears NUMBER check (LifespanYears >= 0) NOT NULL,
                          FOREIGN KEY(Name) REFERENCES Plant
                              ON DELETE CASCADE
);

CREATE TABLE Biennial(
                         Name VARCHAR(255) PRIMARY KEY,
                         VernalizationTemp NUMBER,
                         FOREIGN KEY(Name) REFERENCES Plant
                             ON DELETE CASCADE
);

CREATE TABLE Region(
                       ZoneNum INTEGER GENERATED ALWAYS as IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
                       Description VARCHAR(1000));

CREATE TABLE Forum_SubtopicTopic(
                                    Subtopic VARCHAR(255) PRIMARY KEY,
                                    Topic VARCHAR(255) NOT NULL);

CREATE TABLE Forum(
                      ForumID INT GENERATED ALWAYS as IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
                      Subtopic VARCHAR(255),
                      FOREIGN KEY(Subtopic) REFERENCES Forum_SubtopicTopic
                          ON DELETE CASCADE
);

CREATE TABLE Location_PostalCodeCity(
                                        PostalCode VARCHAR(255) PRIMARY KEY,
                                        City VARCHAR(255));

CREATE TABLE Location_PostalCodeProvince(
                                            PostalCode VARCHAR(255) PRIMARY KEY,
                                            Province VARCHAR(255),
                                            FOREIGN KEY(PostalCode) REFERENCES Location_PostalCodeCity
                                                ON DELETE CASCADE
);

CREATE TABLE Location_PartOf(
                                Street VARCHAR(255),
                                HouseNum INT,
                                PostalCode VARCHAR(255),
                                ZoneNum INT,
                                PRIMARY KEY(Street, HouseNum, PostalCode),
                                FOREIGN KEY(ZoneNum) REFERENCES Region,
                                FOREIGN KEY(PostalCode) REFERENCES Location_PostalCodeProvince
                                    ON DELETE CASCADE
);

CREATE TABLE Suppliers_SellsAt(
                                  SupplierID INT GENERATED ALWAYS as IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
                                  BusinessName VARCHAR(255) NOT NULL,
                                  Street VARCHAR(255) NOT NULL,
                                  HouseNum INT check (HouseNum > 0) NOT NULL,
                                  PostalCode VARCHAR(255) NOT NULL,
                                  FOREIGN KEY(Street, HouseNum, PostalCode) REFERENCES Location_PartOf(Street, HouseNum, PostalCode)
                                      ON DELETE CASCADE
);

CREATE TABLE User_LivesIn(
                             Username VARCHAR(255) PRIMARY KEY,
                             Email VARCHAR(255),
                             Password VARCHAR(255),
                             Street VARCHAR(255) NOT NULL,
                             HouseNum INT check (HouseNum > 0) NOT NULL,
                             PostalCode VARCHAR(255) NOT NULL,
                             FOREIGN KEY(Street, HouseNum, PostalCode) REFERENCES Location_PartOf(Street, HouseNum, PostalCode)
                                 ON DELETE CASCADE
);


CREATE TABLE ActivityLog_Maintain(
                                     LogID INT check (LogID > 0),
                                     Activities VARCHAR(1000),
                                     Status VARCHAR(255),
                                     LogDate DATE,
                                     Username VARCHAR(255),
                                     PRIMARY KEY(LogID, Username),
                                     FOREIGN KEY(Username) REFERENCES User_LivesIn
                                         ON DELETE CASCADE
);

CREATE TABLE Posts_Populate_PostsOn(
                                       PostID INT check (PostID > 0),
                                       Notes VARCHAR(1000) NOT NULL,
                                       PostDate DATE NOT NULL,
                                       ForumID INT check (ForumID > 0),
                                       Username VARCHAR(255),
                                       PRIMARY KEY(PostID, ForumID),
                                       FOREIGN KEY(ForumID) REFERENCES Forum
                                           ON DELETE CASCADE,
                                       FOREIGN KEY(Username) REFERENCES User_LivesIn
                                           ON DELETE SET NULL
);

CREATE TABLE CommunityGarden_LocatedIn(
                                          GardenID INT GENERATED ALWAYS as IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
                                          Name VARCHAR(255),
                                          Street VARCHAR(255) NOT NULL,
                                          HouseNum INT check (HouseNum > 0) NOT NULL,
                                          PostalCode VARCHAR(255) NOT NULL,
                                          FOREIGN KEY(Street, HouseNum, PostalCode) REFERENCES Location_PartOf(Street, HouseNum, PostalCode)
                                              ON DELETE CASCADE
);

CREATE TABLE GrowsIn(
                        Name VARCHAR(255),
                        Type VARCHAR(255),
                        PRIMARY KEY(Name, Type),
                        FOREIGN KEY(Name) REFERENCES Plant
                            ON DELETE CASCADE,
                        FOREIGN KEY(Type) REFERENCES Soil
                            ON DELETE CASCADE
);

CREATE TABLE PurchaseFrom(
                             Name VARCHAR(255),
                             SupplierID INT check (SupplierID > 0),
                             PRIMARY KEY(Name, SupplierID),
                             FOREIGN KEY(Name) REFERENCES Plant
                                 ON DELETE CASCADE,
                             FOREIGN KEY(SupplierID) REFERENCES Suppliers_SellsAt
                                 ON DELETE CASCADE
);

CREATE TABLE BenefitsFrom(
                             Name VARCHAR(255),
                             FertilizerID INT check ( FertilizerID > 0 ),
                             PRIMARY KEY(Name, FertilizerID),
                             FOREIGN KEY(Name) REFERENCES Plant
                                 ON DELETE CASCADE,
                             FOREIGN KEY(FertilizerID) REFERENCES Fertilizer
                                 ON DELETE CASCADE
);

CREATE TABLE Contains(
                         Name VARCHAR(255),
                         LogID INT check ( LogID > 0 ),
                         Username VARCHAR(255),
                         PRIMARY KEY(Name, LogID, Username),
                         FOREIGN KEY(Name) REFERENCES Plant
                             ON DELETE CASCADE,
                         FOREIGN KEY(LogID, Username) REFERENCES ActivityLog_Maintain(LogID, Username)
                             ON DELETE CASCADE,
                         FOREIGN KEY(Username) REFERENCES User_LivesIn
                             ON DELETE CASCADE
);

CREATE TABLE Uses(
                     Username VARCHAR(255),
                     GardenID INT check ( GardenID > 0 ),
                     PRIMARY KEY(GardenID, Username),
                     FOREIGN KEY(GardenID) REFERENCES CommunityGarden_LocatedIn
                         ON DELETE CASCADE,
                     FOREIGN KEY(Username) REFERENCES User_LivesIn
                         ON DELETE CASCADE
);

CREATE TABLE ThrivesIn(
                          Name VARCHAR(255),
                          ZoneNum INTEGER check (ZoneNum >= 0),
                          PRIMARY KEY(ZoneNum, Name),
                          FOREIGN KEY(Name) REFERENCES Plant
                              ON DELETE CASCADE,
                          FOREIGN KEY(ZoneNum) REFERENCES Region
                              ON DELETE CASCADE
);


CREATE OR REPLACE TRIGGER ACTIVITYLOG_TRG
    BEFORE INSERT ON ACTIVITYLOG_MAINTAIN
    FOR EACH ROW
DECLARE
    numLogItemsForUser INT;
BEGIN
    SELECT COUNT(*) INTO numLogItemsForUser FROM ACTIVITYLOG_MAINTAIN WHERE USERNAME=:NEW.USERNAME;

    IF numLogItemsForUser > 0 THEN
        SELECT MAX(AL.LOGID) + 1 INTO :NEW.LOGID FROM ACTIVITYLOG_MAINTAIN AL WHERE USERNAME=:NEW.USERNAME;
    ELSE
        :NEW.LOGID := 1;
    END IF;


    SELECT CURRENT_DATE INTO :NEW.LOGDATE FROM DUAL;
END;


CREATE OR REPLACE TRIGGER POSTS_TRG
    BEFORE INSERT ON POSTS_POPULATE_POSTSON
    FOR EACH ROW
DECLARE
    numPostsInThread INT;
BEGIN
    SELECT COUNT(*) INTO numPostsInThread FROM POSTS_POPULATE_POSTSON WHERE FORUMID=:NEW.FORUMID;

    IF numPostsInThread > 0 THEN
        SELECT MAX(P.POSTID) + 1 INTO :NEW.POSTID FROM POSTS_POPULATE_POSTSON P WHERE FORUMID=:NEW.FORUMID;
    ELSE
        :NEW.POSTID := 1;
    END IF;


    SELECT CURRENT_DATE INTO :NEW.POSTDATE FROM DUAL;
END;

INSERT INTO Soil VALUES('Loamy', 'ACIDIC', 'MEDIUM');
INSERT INTO Soil VALUES('Chalky', 'ALKALINE', 'HIGH');
INSERT INTO Soil VALUES('Peaty', 'ACIDIC', 'LOW');
INSERT INTO Soil VALUES('Silty', 'NEUTRAL', 'LOW');
INSERT INTO Soil VALUES('Sandy', 'NEUTRAL', 'HIGH');
INSERT INTO Soil VALUES('Clay', 'NEUTRAL', 'LOW');

INSERT INTO Fertilizer VALUES(DEFAULT, 'Bone meal', NULL, 'Organic', 'Boney stuff.');
INSERT INTO Fertilizer VALUES(DEFAULT, 'Blood meal', NULL, 'Organic', 'Bloody stuff.');
INSERT INTO Fertilizer VALUES(DEFAULT, 'Brian''s Boost', '16-16-16', 'Slow-release', 'Boosts plant growth, especially buttercups.');
INSERT INTO Fertilizer VALUES(DEFAULT, 'Hamid''s Help', '32-16-16', 'Spike', 'Helps plants grow, especially herbs.');
INSERT INTO Fertilizer VALUES(DEFAULT, 'Ruthie''s Respect', '10-10-10', 'Quick-release', 'Gives plants respect, which they all need.');

INSERT INTO Plant VALUES('Ranunculus asiaticus', NULL, NULL, 3, 6, NULL, NULL, 11, 2, 'Moist, well-draining', 'Full sun', 'Biweekly');
INSERT INTO Plant VALUES('Coriandrum sativum', 4, 5, NULL, NULL, 5, 6, NULL, NULL, 'Moist, well-draining', 'Partial sun', 'Monthly');
INSERT INTO Plant VALUES('Rheum rhabarbarum', NULL, NULL, 6, 6, 4, 4, 10, 3, 'Well-draining', 'Full sun', 'Yearly');
INSERT INTO Plant VALUES('Myosotis sylvatica', 6, 7, 4, 5, NULL, NULL, 11, 3, 'Moist, well-draining', 'Partial sun', 'Yearly');
INSERT INTO Plant VALUES('Papaver orientale', 9, 10, 9, 9, NULL, NULL, 8, 3, 'Moist, well-draining', 'Full sun', 'Yearly');
INSERT INTO Plant VALUES('Brassica oleracea', 6, 6, NULL, NULL, 9, 9, NULL, NULL, 'Well-draining', 'Full sun', 'Biweekly');
INSERT INTO Plant VALUES('Daucus carota', 3, 4, NULL, NULL, 5, 6, NULL, NULL, 'Loose, well-draining', 'Full sun', 'Yearly');
INSERT INTO Plant VALUES('Beta vulgaris', 3, 5, NULL, NULL, 7, 10, NULL, NULL, 'Moist, well-draining', 'Full sun', 'Monthly');
INSERT INTO Plant VALUES('Malus domestica', NULL, NULL, 3, 5, 7, 10, 11, 3, 'Moist, well-draining', 'Full sun', 'Yearly');
INSERT INTO Plant VALUES('Prunus avium', 9, 11, 9, 11, 7, 10, 11, 2, 'Moist, well-draining', 'Partial sun', 'Yearly');
INSERT INTO Plant VALUES('Vaccinium spp.', 5, 5, 4, 5, 6, 8, 10, 3, 'Well-draining', 'Full sun', 'When flowers start blooming and when fruit production begins');
INSERT INTO Plant VALUES('Digitalis purpurea', 3, 5, 5, 6, 7, 8, 10, 3, 'Well-draining', 'Partial sun', 'Yearly, if needed');
INSERT INTO Plant VALUES('Solanum lycopersicum', 3, 5, 3, 4, 7, 9, NULL, NULL, 'Well-draining', 'Full sun', 'Weekly');
INSERT INTO Plant VALUES('Citrullus lanatus', 3, 3, 4, 6, 6, 8, NULL, NULL, 'Well-draining', 'Full sun', 'Monthly');
INSERT INTO Plant VALUES('Zea mays', 4, 4, NULL, NULL, 8, 9, NULL, NULL, 'Moist, well-draining', 'Full sun', 'Once when sowing');

INSERT INTO Annual VALUES('Coriandrum sativum');
INSERT INTO Annual VALUES('Beta vulgaris');
INSERT INTO Annual VALUES('Solanum lycopersicum');
INSERT INTO Annual VALUES('Citrullus lanatus');
INSERT INTO Annual VALUES('Zea mays');

INSERT INTO Perennial VALUES('Ranunculus asiaticus', 3, 10);
INSERT INTO Perennial VALUES('Rheum rhabarbarum', 4, 12);
INSERT INTO Perennial VALUES('Malus domestica', 2, 20);
INSERT INTO Perennial VALUES('Prunus avium', 2, 25);
INSERT INTO Perennial VALUES('Vaccinium spp.', 2, 20);

INSERT INTO Biennial VALUES('Myosotis sylvatica', 0);
INSERT INTO Biennial VALUES('Papaver orientale', 12);
INSERT INTO Biennial VALUES('Brassica oleracea', 10);
INSERT INTO Biennial VALUES('Daucus carota', 5);
INSERT INTO Biennial VALUES('Digitalis purpurea', 7);

INSERT INTO Region VALUES(DEFAULT, '-51.1 to -45.6 degrees Celsius');
INSERT INTO Region VALUES(DEFAULT, '-45.6 to -40 degrees Celsius');
INSERT INTO Region VALUES(DEFAULT, '-40 to -34.4 degrees Celsius');
INSERT INTO Region VALUES(DEFAULT, '-34.4 to -31.7 degrees Celsius');
INSERT INTO Region VALUES(DEFAULT, '-31.7 to -23.3 degrees Celsius');
INSERT INTO Region VALUES(DEFAULT, '-23.3 to -17.8 degrees Celsius');
INSERT INTO Region VALUES(DEFAULT, '-17.8 to -12.2 degrees Celsius');
INSERT INTO Region VALUES(DEFAULT, '-12.2 to -6.7 degrees Celsius');
INSERT INTO Region VALUES(DEFAULT, '-6.7 to -1.1 degrees Celsius');
INSERT INTO Region VALUES(DEFAULT, '-1.1 to 4.4 degrees Celsius');
INSERT INTO Region VALUES(DEFAULT, '4.4 to 10 degrees Celsius');
INSERT INTO Region VALUES(DEFAULT, '10 to 15.6 degrees Celsius');
INSERT INTO Region VALUES(DEFAULT, '15.6 to 21.1 degrees Celsius');

INSERT INTO FORUM_SUBTOPICTOPIC VALUES('Selling: shovel', 'Selling');
INSERT INTO FORUM_SUBTOPICTOPIC VALUES('Selling: flower seeds', 'Selling');
INSERT INTO FORUM_SUBTOPICTOPIC VALUES('Selling: pumpkin seeds', 'Selling');
INSERT INTO FORUM_SUBTOPICTOPIC VALUES('Seeking advice: heatwave killed everything', 'Seeking advice');
INSERT INTO FORUM_SUBTOPICTOPIC VALUES('Miscellaneous: apples vs. oranges', 'Miscellaneous');

INSERT INTO Forum VALUES(DEFAULT, 'Selling: shovel');
INSERT INTO Forum VALUES(DEFAULT, 'Selling: flower seeds');
INSERT INTO Forum VALUES(DEFAULT, 'Selling: pumpkin seeds');
INSERT INTO Forum VALUES(DEFAULT, 'Seeking advice: heatwave killed everything');
INSERT INTO Forum VALUES(DEFAULT, 'Miscellaneous: apples vs. oranges');

INSERT INTO Location_PostalCodeCity VALUES('V1F 2K4', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V5F 3K4', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V5N 5Y4', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V3G 1T7', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('M5F 3K4', 'Toronto');
INSERT INTO Location_PostalCodeCity VALUES('V5R 2D3', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V5F 3D3', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('M1R 2H3', 'Toronto');
INSERT INTO Location_PostalCodeCity VALUES('V1T 2H4', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('M1R 6J1', 'Toronto');
INSERT INTO Location_PostalCodeCity VALUES('V4F 3H6', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V5R 3I2', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V4R 3H6', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V6A 3Z9', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V5N 1T5', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V5T 2B6', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V5Y 1A6', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V5V 2T9', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V6J 1R8', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V5Z 4K2', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V5T 3L8', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V6E 1J2', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V6B 1G8', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V6T 1Z1', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V6M 1W8', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V5L 1B3', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('V5C 2L4', 'Vancouver');
INSERT INTO Location_PostalCodeCity VALUES('M5A 2R6', 'Toronto');
INSERT INTO Location_PostalCodeCity VALUES('M6K 3L4', 'Toronto');
INSERT INTO Location_PostalCodeCity VALUES('M5V 1Y4', 'Toronto');


INSERT INTO Location_PostalCodeProvince VALUES('V1F 2K4', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V5F 3K4', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V5N 5Y4', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V3G 1T7', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('M5F 3K4', 'Ontario');
INSERT INTO Location_PostalCodeProvince VALUES('V5R 2D3', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V5F 3D3', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('M1R 2H3', 'Ontario');
INSERT INTO Location_PostalCodeProvince VALUES('V1T 2H4', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('M1R 6J1', 'Ontario');
INSERT INTO Location_PostalCodeProvince VALUES('V4F 3H6', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V5R 3I2', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V4R 3H6', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V6A 3Z9', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V5N 1T5', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V5T 2B6', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V5Y 1A6', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V5V 2T9', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V6J 1R8', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V5Z 4K2', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V5T 3L8', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V6E 1J2', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V6B 1G8', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V6T 1Z1', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V6M 1W8', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V5L 1B3', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('V5C 2L4', 'British Columbia');
INSERT INTO Location_PostalCodeProvince VALUES('M5A 2R6', 'Ontario');
INSERT INTO Location_PostalCodeProvince VALUES('M6K 3L4', 'Ontario');
INSERT INTO Location_PostalCodeProvince VALUES('M5V 1Y4', 'Ontario');

INSERT INTO Location_PARTOF VALUES('Computer Ct', '1', 'V1F 2K4', 8);
INSERT INTO Location_PARTOF VALUES('Laptop Ln', '2', 'V5F 3K4', 8);
INSERT INTO Location_PARTOF VALUES('Brian''s Rd', '3', 'V5N 5Y4', 8);
INSERT INTO Location_PARTOF VALUES('Hamid''s Rd', '4', 'V3G 1T7', 8);
INSERT INTO Location_PARTOF VALUES('Ruthie''s Rd', '5', 'M5F 3K4', 5);
INSERT INTO Location_PARTOF VALUES('Blooming Blvd', 2, 'V5R 2D3', 8);
INSERT INTO Location_PARTOF VALUES('Seasoning St', 30, 'V5F 3D3', 8);
INSERT INTO Location_PARTOF VALUES('Roasted Rd', 12, 'M1R 2H3', 5);
INSERT INTO Location_PARTOF VALUES('Plant Rd', 45, 'V1T 2H4', 8);
INSERT INTO Location_PARTOF VALUES('Harvest Rd', 65, 'M1R 6J1', 5);
INSERT INTO Location_PARTOF VALUES('Best St.', 1, 'V4F 3H6', 8);
INSERT INTO Location_PARTOF VALUES('Best St.', 2, 'V4F 3H6', 8);
INSERT INTO Location_PARTOF VALUES('Another St.', 12, 'V5R 3I2', 8);
INSERT INTO Location_PARTOF VALUES('Database Dr.', 304, 'V4R 3H6', 3);
INSERT INTO Location_PARTOF VALUES('Malkin Ave', 759, 'V6A 3Z9', 8);
INSERT INTO Location_PARTOF VALUES('E 8th Ave', 1688, 'V5N 1T5', 8);
INSERT INTO Location_PARTOF VALUES('E 10th Ave', 1255, 'V5T 2B6', 8);
INSERT INTO Location_PARTOF VALUES('Spyglass Pl', 1850, 'V5Y 1A6', 8);
INSERT INTO Location_PARTOF VALUES('E 30th Ave', 50, 'V5V 2T9', 8);
INSERT INTO Location_PARTOF VALUES('W 6th Ave #2029', 2053, 'V6J 1R8', 8);
INSERT INTO Location_PARTOF VALUES('Cambie St', 2615, 'V5Z 4K2', 8);
INSERT INTO Location_PARTOF VALUES('Brunswick St', 2390, 'V5T 3L8', 8);
INSERT INTO Location_PARTOF VALUES('Comox St #1165', 1203, 'V6E 1J2', 8);
INSERT INTO Location_PARTOF VALUES('W Hastings St', 210-128, 'V6B 1G8', 8);
INSERT INTO Location_PARTOF VALUES('University Blvd', 6133, 'V6T 1Z1', 8);
INSERT INTO Location_PARTOF VALUES('W 41st Ave', 1008, 'V6M 1W8', 8);
INSERT INTO Location_PARTOF VALUES('Wall St', 2099, 'V5L 1B3', 8);
INSERT INTO Location_PARTOF VALUES('Pender St', 3885, 'V5C 2L4', 8);
INSERT INTO Location_PARTOF VALUES('Shuter St #97', 151, 'M5A 2R6', 8);
INSERT INTO Location_PARTOF VALUES('Milky Way', 87, 'M6K 3L4', 8);
INSERT INTO Location_PARTOF VALUES('Richmond St W', 556, 'M5V 1Y4', 8);

INSERT INTO Suppliers_SellsAt VALUES(DEFAULT, 'Brian''s Buttercups', 'Blooming Blvd', 2, 'V5R 2D3');
INSERT INTO Suppliers_SellsAt VALUES(DEFAULT, 'Hamid''s Herbs', 'Seasoning St', 30, 'V5F 3D3');
INSERT INTO Suppliers_SellsAt VALUES(DEFAULT, 'Ruthie''s Rhubarbs', 'Roasted Rd', 12, 'M1R 2H3');
INSERT INTO Suppliers_SellsAt VALUES(DEFAULT, 'The Local Garden Shop', 'Plant Rd', 45, 'V1T 2H4');
INSERT INTO Suppliers_SellsAt VALUES(DEFAULT, 'The Far Away Garden Shop', 'Harvest Rd', 65, 'M1R 6J1');

INSERT INTO User_LivesIn VALUES('JaneDoe', 'janedoe@gmail.com', '11111111', 'Computer Ct', '1', 'V1F 2K4');
INSERT INTO User_LivesIn VALUES('JohnSmith', 'johnsmith@gmail.com', '22222222', 'Laptop Ln', '2', 'V5F 3K4');
INSERT INTO User_LivesIn VALUES('Brian', 'brian@gmail.com', '33333333', 'Brian''s Rd', '3', 'V5N 5Y4');
INSERT INTO User_LivesIn VALUES('Hamid', 'hamid@gmail.com', '44444444', 'Hamid''s Rd', '4', 'V3G 1T7');
INSERT INTO User_LivesIn VALUES('Ruthie', 'ruthie@gmail.com', '55555555', 'Ruthie''s Rd', '5', 'M5F 3K4');

INSERT INTO ActivityLog_Maintain(ACTIVITIES, STATUS, USERNAME) VALUES('sowed some buttercups', 'very happy', 'Brian');
INSERT INTO ActivityLog_Maintain(ACTIVITIES, STATUS, USERNAME) VALUES('sowed some coriander', 'satisfied', 'Hamid');
INSERT INTO ActivityLog_Maintain(ACTIVITIES, STATUS, USERNAME) VALUES('ate some rhubarb that i grew', 'satiated', 'Ruthie');
INSERT INTO ActivityLog_Maintain(ACTIVITIES, STATUS, USERNAME) VALUES('harvested a watermelon', 'tired', 'Brian');
INSERT INTO ActivityLog_Maintain(ACTIVITIES, STATUS, USERNAME) VALUES('harvested some beets', 'excited', 'Hamid');
INSERT INTO ActivityLog_Maintain(ACTIVITIES, STATUS, USERNAME) VALUES('today was a good day', 'excited', 'Brian');
INSERT INTO ActivityLog_Maintain(ACTIVITIES, STATUS, USERNAME) VALUES('gardening is a lot of work', 'discouraged', 'Hamid');
INSERT INTO ActivityLog_Maintain(ACTIVITIES, STATUS, USERNAME) VALUES('garden looks great today!', 'smiling', 'Ruthie');

INSERT INTO POSTS_POPULATE_POSTSON(NOTES, FORUMID, USERNAME) VALUES('selling a rusty shovel, ten bucks', 1, 'Brian');
INSERT INTO POSTS_POPULATE_POSTSON(NOTES, FORUMID, USERNAME) VALUES('i would like to buy your shovel', 1, 'Hamid');
INSERT INTO POSTS_POPULATE_POSTSON(NOTES, FORUMID, USERNAME) VALUES('selling buttercup seeds. one dollar each', 2, 'Ruthie');
INSERT INTO POSTS_POPULATE_POSTSON(NOTES, FORUMID, USERNAME) VALUES('free pumpkin seeds', 3, 'Brian');
INSERT INTO POSTS_POPULATE_POSTSON(NOTES, FORUMID, USERNAME) VALUES('it''s too hot', 4 ,'Hamid');
INSERT INTO POSTS_POPULATE_POSTSON(NOTES, FORUMID, USERNAME) VALUES('apples > oranges', 5 , 'Ruthie');

INSERT INTO CommunityGarden_LocatedIn VALUES(DEFAULT,  'Strathcona Community Garden', 'Malkin Ave', 759 ,'V6A 3Z9');
INSERT INTO CommunityGarden_LocatedIn VALUES(DEFAULT,  'Lady Bug Community Garden', 'E 8th Ave', 1688 ,'V5N 1T5');
INSERT INTO CommunityGarden_LocatedIn VALUES(DEFAULT,  'China Creek Community Garden', 'E 10th Ave', 1255 ,'V5T 2B6');
INSERT INTO CommunityGarden_LocatedIn VALUES(DEFAULT,  'John McBride Community Garden', 'Spyglass Pl', 1850 ,'V5Y 1A6');
INSERT INTO CommunityGarden_LocatedIn VALUES(DEFAULT,  'Riley Park Community Garden', 'E 30th Ave', 50 ,'V5V 2T9');
INSERT INTO CommunityGarden_LocatedIn VALUES(DEFAULT,  'Kitsilano Community Garden', 'W 6th Ave #2029', 2053 ,'V6J 1R8');
INSERT INTO CommunityGarden_LocatedIn VALUES(DEFAULT,  'Cambie Square Community Garden - SPEC', 'Cambie St', 2615 ,'V5Z 4K2');
INSERT INTO CommunityGarden_LocatedIn VALUES(DEFAULT,  'Brewery Creek Community Garden', 'Brunswick St', 2390 ,'V5T 3L8');
INSERT INTO CommunityGarden_LocatedIn VALUES(DEFAULT,  'Nelson Park Community Garden', 'Comox St #1165', 1203 ,'V6E 1J2');
INSERT INTO CommunityGarden_LocatedIn VALUES(DEFAULT,  'Community Garden Builders', 'W Hastings St', 210-128 ,'V6B 1G8');
INSERT INTO CommunityGarden_LocatedIn VALUES(DEFAULT,  'Roots on the Roof', 'University Blvd', 6133 ,'V6T 1Z1');
INSERT INTO CommunityGarden_LocatedIn VALUES(DEFAULT,  'Vancouver Community Garden', 'W 41st Ave', 1008 ,'V6M 1W8');
INSERT INTO CommunityGarden_LocatedIn VALUES(DEFAULT,  'Wall Street Community Garden', 'Wall St', 2099 ,'V5L 1B3');
INSERT INTO CommunityGarden_LocatedIn VALUES(DEFAULT,  'Heights Garden', 'Pender St', 3885 ,'V5C 2L4');
INSERT INTO CommunityGarden_LocatedIn VALUES(DEFAULT,  'Fred Victor Community Garden', 'Shuter St #97', 151 ,'M5A 2R6');
INSERT INTO CommunityGarden_LocatedIn VALUES(DEFAULT,  'Milky Way Lane Community Garden', 'Milky Way', 87 ,'M6K 3L4');
INSERT INTO CommunityGarden_LocatedIn VALUES(DEFAULT,  'Alex Wilson Community Garden', 'Richmond St W', 556 ,'M5V 1Y4');


INSERT INTO GrowsIn VALUES('Ranunculus asiaticus', 'Sandy');
INSERT INTO GrowsIn VALUES('Coriandrum sativum', 'Loamy');
INSERT INTO GrowsIn VALUES('Rheum rhabarbarum', 'Loamy');
INSERT INTO GrowsIn VALUES('Myosotis sylvatica', 'Loamy');
INSERT INTO GrowsIn VALUES('Papaver orientale', 'Loamy');
INSERT INTO GrowsIn VALUES('Papaver orientale', 'Chalky');
INSERT INTO GrowsIn VALUES('Papaver orientale', 'Sandy');
INSERT INTO GrowsIn VALUES('Brassica oleracea', 'Loamy');
INSERT INTO GrowsIn VALUES('Daucus carota', 'Loamy');
INSERT INTO GrowsIn VALUES('Daucus carota', 'Sandy');
INSERT INTO GrowsIn VALUES('Beta vulgaris', 'Loamy');
INSERT INTO GrowsIn VALUES('Malus domestica', 'Clay');
INSERT INTO GrowsIn VALUES('Malus domestica', 'Loamy');
INSERT INTO GrowsIn VALUES('Prunus avium', 'Loamy');
INSERT INTO GrowsIn VALUES('Vaccinium spp.', 'Peaty');
INSERT INTO GrowsIn VALUES('Digitalis purpurea', 'Loamy');
INSERT INTO GrowsIn VALUES('Solanum lycopersicum', 'Loamy');
INSERT INTO GrowsIn VALUES('Solanum lycopersicum', 'Sandy');
INSERT INTO GrowsIn VALUES('Citrullus lanatus', 'Loamy');
INSERT INTO GrowsIn VALUES('Citrullus lanatus', 'Sandy');
INSERT INTO GrowsIn VALUES('Zea mays', 'Sandy');
INSERT INTO GrowsIn VALUES('Zea mays', 'Loamy');

INSERT INTO PurchaseFrom VALUES('Ranunculus asiaticus', 1);
INSERT INTO PurchaseFrom VALUES('Coriandrum sativum', 2);
INSERT INTO PurchaseFrom VALUES('Rheum rhabarbarum', 2);
INSERT INTO PurchaseFrom VALUES('Rheum rhabarbarum', 3);
INSERT INTO PurchaseFrom VALUES('Papaver orientale', 1);
INSERT INTO PurchaseFrom VALUES('Citrullus lanatus', 3);

INSERT INTO BenefitsFrom VALUES('Ranunculus asiaticus', 3);
INSERT INTO BenefitsFrom VALUES('Ranunculus asiaticus', 4);
INSERT INTO BenefitsFrom VALUES('Coriandrum sativum', 1);
INSERT INTO BenefitsFrom VALUES('Citrullus lanatus', 1);
INSERT INTO BenefitsFrom VALUES('Rheum rhabarbarum', 2);
INSERT INTO BenefitsFrom VALUES('Papaver orientale', 5);

INSERT INTO Contains VALUES('Ranunculus asiaticus', 1, 'Brian');
INSERT INTO Contains VALUES('Coriandrum sativum', 2, 'Hamid');
INSERT INTO Contains VALUES('Rheum rhabarbarum', 3, 'Brian');
INSERT INTO Contains VALUES('Citrullus lanatus', 3, 'Hamid');
INSERT INTO Contains VALUES('Beta vulgaris', 2, 'Ruthie');

INSERT INTO Uses VALUES('Brian', 1);
INSERT INTO Uses VALUES('Brian', 2);
INSERT INTO Uses VALUES('Hamid', 2);
INSERT INTO Uses VALUES('Ruthie', 3);
INSERT INTO Uses VALUES('JaneDoe', 1);

INSERT INTO ThrivesIn VALUES('Ranunculus asiaticus', 7);
INSERT INTO ThrivesIn VALUES('Ranunculus asiaticus', 8);
INSERT INTO ThrivesIn VALUES('Ranunculus asiaticus', 9);
INSERT INTO ThrivesIn VALUES('Ranunculus asiaticus', 10);
INSERT INTO ThrivesIn VALUES('Coriandrum sativum', 2);
INSERT INTO ThrivesIn VALUES('Coriandrum sativum', 3);
INSERT INTO ThrivesIn VALUES('Coriandrum sativum', 4);
INSERT INTO ThrivesIn VALUES('Coriandrum sativum', 5);
INSERT INTO ThrivesIn VALUES('Coriandrum sativum', 6);
INSERT INTO ThrivesIn VALUES('Coriandrum sativum', 7);
INSERT INTO ThrivesIn VALUES('Coriandrum sativum', 8);
INSERT INTO ThrivesIn VALUES('Coriandrum sativum', 9);
INSERT INTO ThrivesIn VALUES('Coriandrum sativum', 10);
INSERT INTO ThrivesIn VALUES('Coriandrum sativum', 11);
INSERT INTO ThrivesIn VALUES('Rheum rhabarbarum', 3);
INSERT INTO ThrivesIn VALUES('Rheum rhabarbarum', 4);
INSERT INTO ThrivesIn VALUES('Rheum rhabarbarum', 5);
INSERT INTO ThrivesIn VALUES('Rheum rhabarbarum', 6);
INSERT INTO ThrivesIn VALUES('Rheum rhabarbarum', 7);
INSERT INTO ThrivesIn VALUES('Rheum rhabarbarum', 8);
INSERT INTO ThrivesIn VALUES('Myosotis sylvatica', 3);
INSERT INTO ThrivesIn VALUES('Myosotis sylvatica', 4);
INSERT INTO ThrivesIn VALUES('Myosotis sylvatica', 5);
INSERT INTO ThrivesIn VALUES('Myosotis sylvatica', 6);
INSERT INTO ThrivesIn VALUES('Myosotis sylvatica', 7);
INSERT INTO ThrivesIn VALUES('Myosotis sylvatica', 8);
INSERT INTO ThrivesIn VALUES('Papaver orientale', 3);
INSERT INTO ThrivesIn VALUES('Papaver orientale', 4);
INSERT INTO ThrivesIn VALUES('Papaver orientale', 5);
INSERT INTO ThrivesIn VALUES('Papaver orientale', 6);
INSERT INTO ThrivesIn VALUES('Papaver orientale', 7);
INSERT INTO ThrivesIn VALUES('Papaver orientale', 8);
INSERT INTO ThrivesIn VALUES('Papaver orientale', 9);
INSERT INTO ThrivesIn VALUES('Brassica oleracea', 2);
INSERT INTO ThrivesIn VALUES('Brassica oleracea', 3);
INSERT INTO ThrivesIn VALUES('Brassica oleracea', 4);
INSERT INTO ThrivesIn VALUES('Brassica oleracea', 5);
INSERT INTO ThrivesIn VALUES('Brassica oleracea', 6);
INSERT INTO ThrivesIn VALUES('Brassica oleracea', 7);
INSERT INTO ThrivesIn VALUES('Brassica oleracea', 8);
INSERT INTO ThrivesIn VALUES('Brassica oleracea', 9);
INSERT INTO ThrivesIn VALUES('Brassica oleracea', 10);
INSERT INTO ThrivesIn VALUES('Daucus carota', 3);
INSERT INTO ThrivesIn VALUES('Daucus carota', 4);
INSERT INTO ThrivesIn VALUES('Daucus carota', 5);
INSERT INTO ThrivesIn VALUES('Daucus carota', 6);
INSERT INTO ThrivesIn VALUES('Daucus carota', 7);
INSERT INTO ThrivesIn VALUES('Daucus carota', 8);
INSERT INTO ThrivesIn VALUES('Daucus carota', 9);
INSERT INTO ThrivesIn VALUES('Daucus carota', 10);
INSERT INTO ThrivesIn VALUES('Beta vulgaris', 2);
INSERT INTO ThrivesIn VALUES('Beta vulgaris', 3);
INSERT INTO ThrivesIn VALUES('Beta vulgaris', 4);
INSERT INTO ThrivesIn VALUES('Beta vulgaris', 5);
INSERT INTO ThrivesIn VALUES('Beta vulgaris', 6);
INSERT INTO ThrivesIn VALUES('Beta vulgaris', 7);
INSERT INTO ThrivesIn VALUES('Beta vulgaris', 8);
INSERT INTO ThrivesIn VALUES('Beta vulgaris', 9);
INSERT INTO ThrivesIn VALUES('Beta vulgaris', 10);
INSERT INTO ThrivesIn VALUES('Beta vulgaris', 11);
INSERT INTO ThrivesIn VALUES('Malus domestica', 3);
INSERT INTO ThrivesIn VALUES('Malus domestica', 4);
INSERT INTO ThrivesIn VALUES('Malus domestica', 5);
INSERT INTO ThrivesIn VALUES('Malus domestica', 6);
INSERT INTO ThrivesIn VALUES('Malus domestica', 7);
INSERT INTO ThrivesIn VALUES('Malus domestica', 8);
INSERT INTO ThrivesIn VALUES('Prunus avium', 4);
INSERT INTO ThrivesIn VALUES('Prunus avium', 5);
INSERT INTO ThrivesIn VALUES('Prunus avium', 6);
INSERT INTO ThrivesIn VALUES('Prunus avium', 7);
INSERT INTO ThrivesIn VALUES('Vaccinium spp.', 4);
INSERT INTO ThrivesIn VALUES('Vaccinium spp.', 5);
INSERT INTO ThrivesIn VALUES('Vaccinium spp.', 6);
INSERT INTO ThrivesIn VALUES('Vaccinium spp.', 7);
INSERT INTO ThrivesIn VALUES('Digitalis purpurea', 4);
INSERT INTO ThrivesIn VALUES('Digitalis purpurea', 5);
INSERT INTO ThrivesIn VALUES('Digitalis purpurea', 6);
INSERT INTO ThrivesIn VALUES('Digitalis purpurea', 7);
INSERT INTO ThrivesIn VALUES('Digitalis purpurea', 8);
INSERT INTO ThrivesIn VALUES('Digitalis purpurea', 9);
INSERT INTO ThrivesIn VALUES('Solanum lycopersicum', 5);
INSERT INTO ThrivesIn VALUES('Solanum lycopersicum', 6);
INSERT INTO ThrivesIn VALUES('Solanum lycopersicum', 7);
INSERT INTO ThrivesIn VALUES('Solanum lycopersicum', 8);
INSERT INTO ThrivesIn VALUES('Citrullus lanatus', 2);
INSERT INTO ThrivesIn VALUES('Citrullus lanatus', 3);
INSERT INTO ThrivesIn VALUES('Citrullus lanatus', 4);
INSERT INTO ThrivesIn VALUES('Citrullus lanatus', 5);
INSERT INTO ThrivesIn VALUES('Citrullus lanatus', 6);
INSERT INTO ThrivesIn VALUES('Citrullus lanatus', 7);
INSERT INTO ThrivesIn VALUES('Citrullus lanatus', 8);
INSERT INTO ThrivesIn VALUES('Citrullus lanatus', 9);
INSERT INTO ThrivesIn VALUES('Citrullus lanatus', 10);
INSERT INTO ThrivesIn VALUES('Citrullus lanatus', 11);
INSERT INTO ThrivesIn VALUES('Zea mays', 4);
INSERT INTO ThrivesIn VALUES('Zea mays', 5);
INSERT INTO ThrivesIn VALUES('Zea mays', 6);
INSERT INTO ThrivesIn VALUES('Zea mays', 7);
INSERT INTO ThrivesIn VALUES('Zea mays', 8);
