<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- @author Karanveer -->
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Sollers Trading System - Add Address</title>
	</head>
	<body>      
		<h1>Add Address</h1>
		<form name="addaddressform" method="post" onsubmit="return checker()" action="AddAddress">
			<fieldset>
				<legend>Billing Address</legend>
				<label for="frmLine1">Line 1:</label>
				<input type="text" name="line1" id="frmLine1" pattern="^\d{1,5}(\s[A-Z][a-z]+)*([\s\.\-[A-Za-z]+)*" title="Must begin with street number and first word must be capital" required/><br>
				
				<label for="frmLine2">Line 2:</label>
				<input type="text" name="line2" id="frmLine2"/><br>
				
				<label for="frmCity">City:</label>
				<input type="text" name="city" id="frmCity" pattern="^[A-Z][a-z\.]+([\s\-][A-Z][a-z]+)*$" title="Capitalized word(s). No numbers" required/><br>
				
				<label for="frmState">State:</label>
						<select id="frmState" name="state">
							<option value="AL">Alabama</option>
							<option value="AK">Alaska</option>
							<option value="AZ">Arizona</option>
							<option value="AR">Arkansas</option>
							<option value="CA">California</option>
							<option value="CO">Colorado</option>
							<option value="CT">Connecticut</option>
							<option value="DE">Delaware</option>
							<option value="DC">District Of Columbia</option>
							<option value="FL">Florida</option>
							<option value="GA">Georgia</option>
							<option value="HI">Hawaii</option>
							<option value="ID">Idaho</option>
							<option value="IL">Illinois</option>
							<option value="IN">Indiana</option>
							<option value="IA">Iowa</option>
							<option value="KS">Kansas</option>
							<option value="KY">Kentucky</option>
							<option value="LA">Louisiana</option>
							<option value="ME">Maine</option>
							<option value="MD">Maryland</option>
							<option value="MA">Massachusetts</option>
							<option value="MI">Michigan</option>
							<option value="MN">Minnesota</option>
							<option value="MS">Mississippi</option>
							<option value="MO">Missouri</option>
							<option value="MT">Montana</option>
							<option value="NE">Nebraska</option>
							<option value="NV">Nevada</option>
							<option value="NH">New Hampshire</option>
							<option value="NJ">New Jersey</option>
							<option value="NM">New Mexico</option>
							<option value="NY">New York</option>
							<option value="NC">North Carolina</option>
							<option value="ND">North Dakota</option>
							<option value="OH">Ohio</option>
							<option value="OK">Oklahoma</option>
							<option value="OR">Oregon</option>
							<option value="PA">Pennsylvania</option>
							<option value="RI">Rhode Island</option>
							<option value="SC">South Carolina</option>
							<option value="SD">South Dakota</option>
							<option value="TN">Tennessee</option>
							<option value="TX">Texas</option>
							<option value="UT">Utah</option>
							<option value="VT">Vermont</option>
							<option value="VA">Virginia</option>
							<option value="WA">Washington</option>
							<option value="WV">West Virginia</option>
							<option value="WI">Wisconsin</option>
							<option value="WY">Wyoming</option>
						</select><br>	
				
				<label for="frmZip">Zip(+4 optional)</label>
				<input type="text" name="zip" id="frmZip" pattern="\d{5}" title="Must be five digits long" required/>-<input type="text" name="zip4" pattern="\d{4}" title="Must be four digits long"/><br>
				
			</fieldset>	<br>
		
			<input type="checkbox" name="sameMailing" value="sameMailing" onclick="FillMailing(this.form)"><em>Check this box if mailing address is same as billing</em><br><br>

			<fieldset>
				<legend>Mailing Address</legend>
				
				<input type="checkbox" name="poBox" onclick="EnablePoBox(this.form)"/><em>PO Box</em><br>
				
				<label for="mPOBox">PO Box</label>
				<input type="text" name="mPOBoxNum" id="mPOBox" pattern="\d{1,4}" disabled/><br>
				
				<label for="mFrmLine1">Line 1:</label>
				<input type="text" name="mLine1" id="mFrmLine1" pattern="^\d{1,}(\s[A-Z][a-z?\.?]*)*" title="Must begin with street number and words must be capitalized" required/><br>
				
				<label for="mfrmLine2">Line 2:</label>
				<input type="text" name="mLine2" id="mfrmLine2"/><br>
				
				<label for="mfrmCity">City:</label>
				<input type="text" name="mCity" id="mfrmCity" pattern="^[A-Z][a-z\.]+([\s\-][A-Z][a-z]+)*$" title="Capitalized word(s). No numbers" required/><br>
				
				<label for="mfrmState">State:</label>
						<select id="mfrmState" name="mState">
							<option value="AL">Alabama</option>
							<option value="AK">Alaska</option>
							<option value="AZ">Arizona</option>
							<option value="AR">Arkansas</option>
							<option value="CA">California</option>
							<option value="CO">Colorado</option>
							<option value="CT">Connecticut</option>
							<option value="DE">Delaware</option>
							<option value="DC">District Of Columbia</option>
							<option value="FL">Florida</option>
							<option value="GA">Georgia</option>
							<option value="HI">Hawaii</option>
							<option value="ID">Idaho</option>
							<option value="IL">Illinois</option>
							<option value="IN">Indiana</option>
							<option value="IA">Iowa</option>
							<option value="KS">Kansas</option>
							<option value="KY">Kentucky</option>
							<option value="LA">Louisiana</option>
							<option value="ME">Maine</option>
							<option value="MD">Maryland</option>
							<option value="MA">Massachusetts</option>
							<option value="MI">Michigan</option>
							<option value="MN">Minnesota</option>
							<option value="MS">Mississippi</option>
							<option value="MO">Missouri</option>
							<option value="MT">Montana</option>
							<option value="NE">Nebraska</option>
							<option value="NV">Nevada</option>
							<option value="NH">New Hampshire</option>
							<option value="NJ">New Jersey</option>
							<option value="NM">New Mexico</option>
							<option value="NY">New York</option>
							<option value="NC">North Carolina</option>
							<option value="ND">North Dakota</option>
							<option value="OH">Ohio</option>
							<option value="OK">Oklahoma</option>
							<option value="OR">Oregon</option>
							<option value="PA">Pennsylvania</option>
							<option value="RI">Rhode Island</option>
							<option value="SC">South Carolina</option>
							<option value="SD">South Dakota</option>
							<option value="TN">Tennessee</option>
							<option value="TX">Texas</option>
							<option value="UT">Utah</option>
							<option value="VT">Vermont</option>
							<option value="VA">Virginia</option>
							<option value="WA">Washington</option>
							<option value="WV">West Virginia</option>
							<option value="WI">Wisconsin</option>
							<option value="WY">Wyoming</option>
						</select><br>	
				
				<label for="mfrmZip">Zip(+4 optional)</label>
				<input type="text" name="mZip" id="mFrmZip" pattern="\d{5}" title="Must be five digits long" required/>-<input type="text" name="mZip4" pattern="\d{4}" title="Must be four digits long"/><br>
			
			</fieldset>
			
			<br>
			<input type="submit" name="submit"/>
		</form>
	</body>
	<script>
		function FillMailing(form) {
			  if(form.sameMailing.checked == true && form.poBox.checked == false) {
			    form.mLine1.value = form.line1.value;
			    form.mLine2.value = form.line2.value;
			    form.mCity.value = form.city.value;
			    form.mState.value = form.state.value;
			    form.mZip.value = form.zip.value;
			    form.mZip4.value = form.zip4.value;
			  }
			  else {
				  form.mLine1.value = "";
				  form.mLine2.value = "";
				  form.mCity.value = "";
				  form.mState.value = "";
				  form.mZip.value = "";
				  form.mZip4.value = "";
			  }
		}
		function EnablePoBox(form) {
			if (form.poBox.checked == true) {
				// require poBox, disable and wipe lines
				form.mPOBoxNum.disabled = false;
				form.mPOBoxNum.required = true;
				form.mLine1.disabled = true;
				form.mLine1.required = false;
				form.mLine2.disabled = true;
				form.mLine2.required = false;
				form.mLine1.value = "";
				form.mLine2.value = "";
			}
			else {
				// require lines, disable and wipe poBox
				form.mPOBoxNum.disabled = true;
				form.mPOBoxNum.value = "";
				form.mPOBoxNum.required = false;
				form.mLine1.disabled = false;
				form.mLine1.required = true;
				form.mLine2.disabled = false;
			}
		}
	</script>
</html>