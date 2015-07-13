if (object_id('ChecksInsterClimbed', 'TR') is not null)
    drop trigger ChecksInsterClimbed
go

CREATE TRIGGER ChecksInsterClimbed

on climbed
   after insert
as 
begin
   declare @tripid INT,
           @peakname varchar(30),
		   @climbedtime datetime,
		   @gapday INT;
   select @tripid = s.TRIP_ID, @peakname =s.PEAK, @climbedtime = s.WHEN_CLIMBED 
   from inserted as s;
   select @gapday = min( datediff(d,c.WHEN_CLIMBED,@climbedtime))
   from CLIMBED as c inner join inserted on  c.TRIP_ID=@tripid
   where datediff(d,c.WHEN_CLIMBED,@climbedtime)>=20
    if (@gapday>=20)   
       begin
	      print 'WARNING: The climb you inserted is ';
		  print @gapday ;
		  Print 'days from the closest existing climb in trip';
		  print @tripid;
		  print '.';
		  print('Continue to be inserted successful!')
	   end
   else
      begin
	     print('Inserted successful!')
	  end
end

--testing 
INSERT INTO  climbed VALUES (1,  'Kearsarge Peak', '06/28/2002'); 
INSERT INTO  climbed VALUES (6,  'Mount Guyot', '06/21/2002'); 
INSERT INTO  climbed VALUES (23, 'Lion Rock', '08/09/2004');
INSERT INTO  climbed VALUES (23,  'Mount Williamson', '06/09/2004'); 
INSERT INTO  climbed VALUES (29, 'Lion Rock', '06/09/2004');
