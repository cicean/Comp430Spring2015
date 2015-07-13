IF OBJECT_ID (N'findmostsimilar') IS NOT NULL
   DROP procedure findmostsimilar
GO


create procedure findmostsimilar

as

  begin

  declare climberpairlist cursor for
      select c1.name, c2.name
	  from climber as c1, climber as c2
	  where c1.name < c2.NAME ;

	  declare @climber1 varchar(8000),
              @climber2 varchar(8000);
	  declare @maxsimilarity int;

	  set @maxsimilarity=0;

	  declare @climberpeaksimilaritytable table (climber1 varchar(8000),climber2 varchar(8000),similarityvalue int)

	  open climberpairlist;
	  fetch climberpairlist into @climber1,@climber2;
	  while(@@FETCH_STATUS=0)
	  begin

	     set @maxsimilarity= (select dbo.LongestCommonSubsequence(@climber1,@climber2));
	      insert into @climberpeaksimilaritytable values (@climber1,@climber2,@maxsimilarity);
		  fetch climberpairlist into @climber1,@climber2;
		 
	  end
	  close climberpairlist
	  deallocate climberpairlist

	  print @climber1
	  print @climber2
	  print @maxsimilarity

	  

	  declare @maxclimber1 Varchar(8000),
	          @maxclimber2 Varchar(8000);
	  declare @maxpeaksclimbed int;

	  set @maxpeaksclimbed = (select top(1) similarityvalue from @climberpeaksimilaritytable order by similarityvalue desc)

	  declare resultset cursor for
	  select climber1,climber2
	  from @climberpeaksimilaritytable
	  where similarityvalue=@maxpeaksclimbed;

	  select * from @climberpeaksimilaritytable where similarityvalue=@maxpeaksclimbed;

	  open resultset;
	  fetch resultset into @climber1,@climber2;
	  while (@@FETCH_STATUS=0)
	  begin
	     print 'test'
		 print @climber1
		 print @climber2
	     print 'The two most similar climbers are ' + @climber1 + 'and '+ @climber2 +'.'
		 print 'The longest sequence of peak ascents common to both is:' 

		 select cl1.peak
		 from (
		   select p.NAME, c.PEAK
		   from CLIMBED as c inner join PARTICIPATED as p on c.TRIP_ID = p.TRIP_ID
		  where p.NAME=@climber1 ) as cl1 inner join (select p.NAME, c.PEAK
		   from CLIMBED as c inner join PARTICIPATED as p on c.TRIP_ID = p.TRIP_ID
		  where p.NAME=@climber2 ) as cl2 on cl1.peak = cl2.peak
       
	    
      -- fetch resultset into @climber1,@climber2;
	  end

	  close resultset;
	  deallocate resultset;

   end
   
   EXECUTE findmostsimilar;