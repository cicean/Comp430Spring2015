IF OBJECT_ID (N'LongestCommonSubsequence') IS NOT NULL
   DROP FUNCTION LongestCommonSubsequence
GO


create function LongestCommonSubsequence
	 (	@climber1 varchar(8000),
		@climber2 varchar(8000)
	 )
returns int
WITH SCHEMABINDING 
as
begin
	declare @fPeaks varchar (8000);
	declare @sPeaks varchar (8000);

	declare @fPeaksTABLE table ( peakId int PRIMARY KEY IDENTITY, peak varchar (8000));
	declare @sPeaksTABLE table ( peakId int PRIMARY KEY IDENTITY, peak varchar (8000));

	insert into @fPeaksTABLE 
			select  c.PEAK 
			from	dbo.PARTICIPATED as p, dbo.CLIMBED  as c
			where   p.trip_id = c.trip_id and p.name = @climber1
			ORDER by c.WHEN_CLIMBED	 ;

	insert into @sPeaksTABLE
			select  c.PEAK 
			from	dbo.PARTICIPATED as p, dbo.CLIMBED  as c
			where p.TRIP_ID = c.TRIP_ID and p.NAME = @climber2
			order by c.WHEN_CLIMBED	;

	declare @fPeakClimbedCount int;
	declare @sPeakClimbedCount int;
	declare @longestSequencePeakTABLE table ( i int,  j int, value int);
	declare @longestSequence int;

	declare @i int;
	declare @j int;
	set @i = 0;
 	set @j = 0;

	set @fPeakClimbedCount = ( select COUNT(*) from @fPeaksTABLE);
	set @sPeakClimbedCount = ( select COUNT(*) from @sPeaksTABLE);

	
	if ( @fPeakClimbedCount = 0  ) or (@sPeakClimbedCount = 0)
		begin
			return 0
		end
	else
		begin
				while (@i<= @fPeakClimbedCount)
				  begin		
						insert into @longestSequencePeakTABLE values (@i,0,0);
						set @i = @i + 1;
				  end
  				while (@j<= @sPeakClimbedCount)
				  begin		
						insert into @longestSequencePeakTABLE values (0,@j,0);
						set @j = @j + 1;
				  end

				set @i = 1;
				set @j = 1;

				declare @tempValue int;
				declare @tempPeak1 varchar(8000);
				declare @tempPeak2 varchar(8000);
				declare @value1 int;
				declare @value2 int;
				declare @value3 int;

				while (@i<= @fPeakClimbedCount)
				begin	
				  set @tempPeak1 = (select peak from @fPeaksTABLE where peakId = @i);
				  while (@j<= @sPeakClimbedCount)
					begin
						set @tempPeak2 = (select peak from @sPeaksTABLE where peakId = @j);
						IF (@tempPeak1 = @tempPeak2)
							begin
								set @value1 = (	select Top(1) value from @longestSequencePeakTABLE where i = @i-1 and j = @j-1 );
								set @value1 = @value1 +1 ;
								insert into @longestSequencePeakTABLE values (@i,@j,@value1);
							end
						else
							begin
								set @value2 = (	select Top(1) value from @longestSequencePeakTABLE where i = @i and j = @j-1 );
								set @value3 = (	select Top(1) value from @longestSequencePeakTABLE where i = @i-1 and j = @j );
								IF (@value2 < @value3)
									begin
										insert into @longestSequencePeakTABLE values (@i,@j,@value3);
										
									end
								else
									begin
										insert into @longestSequencePeakTABLE values (@i,@j,@value2);
									end
							end
						set @j = @j + 1;
					end
				  set @j = 1;
				  set @i = @i + 1;
				end

			set @longestSequence = (select Top (1) value from @longestSequencePeakTABLE where i = @fPeakClimbedCount and j = @sPeakClimbedCount ); 
		end
	
	return @longestSequence;
end
