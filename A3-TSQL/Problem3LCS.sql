IF OBJECT_ID (N'LongestCommonSubsequence') IS NOT NULL
   DROP FUNCTION LongestCommonSubsequence
GO

create function LongestCommonSubsequence
(
  @climber1 Varchar(8000),
  @climber2 Varchar(8000)
)
returns int
WITH SCHEMABINDING AS

begin
  
     declare @climber1Peaks Varchar(8000),
	         @climber2peaks Varchar(8000);
      declare @climber1peakstable table(peakid int primary key identity, peakname VARCHAR (8000) );
	  declare @climber2peakstable table (peakId int primary key identity, peakname VARCHAR (8000));

	  insert into @climber1peakstable -- insert the frist climber1 climbed mountain name in to table
	     select c.peak
		 from dbo.PARTICIPATED as p inner join dbo.CLIMBED as c on p.TRIP_ID=c.TRIP_ID
		 where p.NAME = @climber1
		 order by c.peak asc

	   insert into @climber2peakstable -- insert the frist climber2 climbed mountain name in to table
	       select c.peak
		 from dbo.PARTICIPATED as p inner join dbo.CLIMBED as c on p.TRIP_ID=c.TRIP_ID
		 where p.NAME = @climber2
		 order by c.peak asc

	   declare @climber1peakclimedcount int,
	           @climber2peakclimedcount int;
	   declare @LCSpeakcounttable table (i INT,j INT,countvalue int) -- create LCS table to find the top max LCS
	   declare @LCSresult INT;

	   declare @i int, @j int;
	   set @i=0;
	   set @j=0;

	   select @climber1peakclimedcount = count(*) from @climber1peakstable
	   select @climber2peakclimedcount = count(*) from @climber2peakstable

	   if (@climber1peakclimedcount = 0) or (@climber2peakclimedcount = 0)

	   begin

	       return 0

	   end

	   else
	   begin

	       while(@i<=@climber1peakclimedcount)
		   begin
		    
			insert into @LCSpeakcounttable values (@i,0,0);
						SET @i = @i + 1;

			end
			while(@j<=@climber2peakclimedcount)
			begin

			insert into @LCSpeakcounttable values (0,@j,0);
						SET @j = @j + 1;
			end

			set @i=1;
			set @j=1;

			declare @tempcountvalue int,
			        @temppeak1 VARCHAR(8000),
					@temppeak2 VARCHAR(8000);
			declare @tempvalue1 int,
			        @tempvalue2 int,
					@tempvalue3 int;
             while (@i<=@climber1peakclimedcount)
			 begin

			    select @temppeak1= c1.peakname from @climber1peakstable as c1 where c1.peakid=@i
				  
				  while (@j<@climber2peakclimedcount) 
				   begin
				 select @temppeak2 = c2.peakname from @climber2peakstable as c2 where c2.peakId=@j
				    if(@temppeak1=@temppeak2)
					  begin

					  set @tempvalue1= (select Top(1)countvalue from @LCSpeakcounttable WHERE i = @i-1 and j = @j-1 );
					  set @tempvalue1= @tempvalue1+1
					  insert into @LCSpeakcounttable values(@i,@j,@tempvalue1);

					  end

					 else
					    begin
						  set @tempvalue2 =(select Top(1)countvalue from @LCSpeakcounttable WHERE i = @i and j = @j-1 );
						  set @tempvalue3 =(select Top(1)countvalue from @LCSpeakcounttable WHERE i = @i-1 and j = @j );
						  if(@tempvalue2 < @tempvalue3)
						   begin
						      
							  insert into @LCSpeakcounttable values(@i,@j,@tempvalue3)
							  end
						 else
						   begin
						   insert into @LCSpeakcounttable values(@i,@j,@tempvalue2)
						   end
			        end
				set @j=@j+1;
			  end
			set @j=1;
			set @i=@i+1;

	   end
	   set @LCSresult = (select top(1)countvalue from @LCSpeakcounttable where i=@climber1peakclimedcount and j=@climber2peakclimedcount)
end
  return @LCSresult;
end