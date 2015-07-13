if (object_id('ChecksDelPart', 'TR') is not null)
    drop trigger ChecksDelPart
go

CREATE TRIGGER ChecksDelPart
on PARTICIPATED
   after delete
   as
begin
    declare @tripid_part INT,@name_part VARCHAR(30);
	select @tripid_part = d.TRIP_ID,@name_part = d.NAME
	from deleted as d
	if (not exists(select * 
	         from PARTICIPATED as p
			 where p.TRIP_ID = @tripid_part))
	   begin
	       delete  from CLIMBED
		   where CLIMBED.TRIP_ID = @tripid_part
		   print 'Delete from Climned Table';
		  end
    else
	   begin
	   print 'No delete item.';
	   end
	end

DELETE FROM  participated WHERE  trip_id =  12; SELECT COUNT(*)  FROM  climbed WHERE  trip_id =  12;

DELETE FROM  participated WHERE  trip_id =  13  AND name <>  'ELIZABETH'; SELECT COUNT(*)  FROM  climbed WHERE  trip_id =  13;

DELETE FROM  participated WHERE  name =  'ELIZABETH'; SELECT COUNT(*)  FROM  climbed WHERE  trip_id =  13;

SELECT COUNT  (DISTINCT trip_id) FROM  climbed; DELETE FROM  participated WHERE  trip_id  IN
(SELECT trip_id FROM  participated WHERE  name =  'LINDA'); SELECT COUNT  (DISTINCT trip_id) FROM  climbed;


	        

	

     