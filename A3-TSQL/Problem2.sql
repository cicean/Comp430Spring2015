if (object_id('Catchesallinserts', 'TR') is not null)
    drop trigger Catchesallinserts
go

create trigger Catchesallinserts
on climbed
instead of insert
as
begin
   declare @insertpeak varchar(8000),
           @existedpeakname varchar(8000),
		   @cutoffvalue INT,
		   @value int;

		   select @cutoffvalue = cutoff from ed_cutoff
		   select @insertpeak = peak from inserted 

   declare peaknames cursor for
   select name from peak;
   declare @peakeditesemblance table ( peakname varchar(800)unique,eidtdistance int);

   open peaknames;
   fetch peaknames into @existedpeakname;

   while (@@FETCH_STATUS=0)
   begin
      set @value = (select dbo.levenshteinDistance(@insertpeak,@existedpeakname))
	  insert into @peakeditesemblance values (@existedpeakname,@value);
	  fetch peaknames into @existedpeakname;
	end

	

	close peaknames;


	deallocate peaknames;

	declare resultpeakset cursor for
   select Top(1) peakName , eidtdistance
        from @peakeditesemblance
		order by eidtdistance ASC

	open resultpeakset;
	fetch resultpeakset into @existedpeakname,@value;

	print @value

	if (@value=0)
	begin
	    insert into climbed
		    select * from inserted
		print 'succesfully inserted'
	end

	else 
	   begin
	     if (@value <= @cutoffValue)
		   begin
		   print 'ERROR: Inserted peak name'+  @insertpeak +'does not match any in
the database.'+ @existedpeakname + ' is used instead.'
         declare @date DATE;
		 declare @tripID int;

		 select @date = when_climbed from inserted
		 select @tripid = trip_id from inserted

		 insert into CLIMBED values(@tripid,@existedpeakname,@date)
		  end
	    else
		  begin
		    print 'ERROR: Inserted peak name ' + '"'+@insertpeak +'"'+'does not closely match
any in the database, and so the insert is rejected.'
            rollback tran;
		  end
		end
	PRINT 'Peak closest is ' + @existedpeakname + '  its distance is : '+ cast(@value as varchar(10));	

	close resultpeakset
	deallocate resultpeakset;



end




---- testing
INSERT INTO climbed VALUES (30, 'North Guard', '09/06/2002');
INSERT INTO climbed VALUES (30, 'Home Nose', '09/06/2002');
SELECT * FROM climbed WHERE trip_id = 30;

INSERT INTO climbed VALUES (31, 'Moses Mount', '09/06/2002');
INSERT INTO climbed VALUES (31, 'Olancha Mountain', '09/06/2002');
INSERT INTO climbed VALUES (31, 'Mt. Hitchcock', '09/06/2002');
INSERT INTO climbed VALUES (31, 'Mt Hitchcock', '09/06/2002');
INSERT INTO climbed VALUES (31, 'Milestoan Mounten', '09/06/2002');
INSERT INTO climbed VALUES (31, 'Milestoan Mountan', '09/06/2002');
SELECT * FROM climbed WHERE trip_id = 31;